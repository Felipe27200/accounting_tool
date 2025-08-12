package com.accounting.accounting_tool.controller;

import com.accounting.accounting_tool.common.DateFormatValidator;
import com.accounting.accounting_tool.config.ModelMapperConfig;
import com.accounting.accounting_tool.dto.account.CreateAccountDTO;
import com.accounting.accounting_tool.dto.account.FilterAccountDTO;
import com.accounting.accounting_tool.dto.account.SelectAccountDTO;
import com.accounting.accounting_tool.entity.Account;
import com.accounting.accounting_tool.entity.Category;
import com.accounting.accounting_tool.entity.FinancialStatement;
import com.accounting.accounting_tool.error_handling.exception.GeneralException;
import com.accounting.accounting_tool.response.BasicResponse;
import com.accounting.accounting_tool.service.AccountService;
import com.accounting.accounting_tool.service.CategoryService;
import com.accounting.accounting_tool.service.FinancialStatementService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(path = "${apiPrefix}/accounts")
public class AccountController
{
    private final CategoryService categoryService;
    private final AccountService accountService;
    private final DateFormatValidator dateValidator;
    private final ModelMapperConfig modelMapper;
    private final FinancialStatementService financialStatementService;

    @Autowired
    public AccountController(
        AccountService accountService,
        DateFormatValidator dateValidator,
        ModelMapperConfig modelMapper,
        CategoryService categoryService,
        FinancialStatementService financialStatementService
    ) {
        this.accountService = accountService;
        this.dateValidator = dateValidator;
        this.modelMapper = modelMapper;
        this.categoryService = categoryService;
        this.financialStatementService = financialStatementService;
    }

    @PostMapping("/create")
    public ResponseEntity<Account> createAccount(@Valid @RequestBody CreateAccountDTO accountDTO)
    {
        this.validateAccount(accountDTO);
        Account account = this.mapDtoToEntity(accountDTO);
        String username = getAuthUsername();

        account.setCategory(this.categoryService.findById(accountDTO.getCategoryId(), username));
        FinancialStatement financialStatement =
                this.financialStatementService
                        .findByIdAndUser(
                                account.getFinancialStatement().getId(),
                                username
                        );

        return new ResponseEntity<>(this.accountService.save(account, username, financialStatement), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SelectAccountDTO> update(@Valid @RequestBody CreateAccountDTO accountDTO, @PathVariable Long id)
    {
        this.validateAccount(accountDTO);
        String username = getAuthUsername();

        Account account = this.mapDtoToEntity(accountDTO, id);
        account.setCategory(this.categoryService.findById(accountDTO.getCategoryId(), getAuthUsername()));

        FinancialStatement financialStatement =
                this.financialStatementService
                        .findByIdAndUser(
                                account.getFinancialStatement().getId(),
                                username
                        );

        return new ResponseEntity<>(this.accountService.update(account, username, financialStatement), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BasicResponse> deleteById(@PathVariable Long id)
    {
        String message = this.accountService.deleteById(id, getAuthUsername());
        BasicResponse<String> response = new BasicResponse<>(message, "successful");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SelectAccountDTO> getById(@PathVariable Long id)
    {
        return new ResponseEntity<>(this.accountService.findByIdAndUser(id, getAuthUsername()), HttpStatus.OK);
    }

    @GetMapping("/search-statement-id/{statementId}")
    public ResponseEntity<List<SelectAccountDTO>> findByStatementId(@PathVariable(name = "statementId") Long statementId)
    {
        return new ResponseEntity<>(this.accountService.findByStatementId(statementId, getAuthUsername()), HttpStatus.OK);
    }

    @GetMapping("/search-date/{dateStr}")
    public ResponseEntity<List<SelectAccountDTO>> getByDate(@PathVariable String dateStr)
    {
        if (!dateValidator.isValidDate(dateStr))
            throw new GeneralException("The date: " + dateStr + " is not a format valid. Must be YYYY-mm-DD");

        Date date = dateValidator.converToDate(dateStr);

        return new ResponseEntity<>(this.accountService.findByDateAndUser(date, getAuthUsername()), HttpStatus.OK);
    }

    @GetMapping("/date-range/{startDate}/to/{endDate}")
    public ResponseEntity<List<SelectAccountDTO>> getByRangeDate(@PathVariable String startDate, @PathVariable String endDate)
    {
        List<SelectAccountDTO> accountDTOList = this.accountService
            .findByDateRangeAndUser(startDate, endDate, getAuthUsername());

        return new ResponseEntity<>(accountDTOList, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<SelectAccountDTO>> getAllByUser()
    {
        return new ResponseEntity<>(this.accountService.findAllByUser(getAuthUsername()), HttpStatus.OK);
    }

    @PostMapping("/filter-account")
    public ResponseEntity<List<SelectAccountDTO>> filterAccount(@RequestBody FilterAccountDTO filterAccountDTO)
    {
        if (filterAccountDTO.getInitDate() == null && filterAccountDTO.getEndDate() == null
            && filterAccountDTO.getStatementId() == null && filterAccountDTO.getCategoryList() == null)
        {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }

        if (filterAccountDTO.getInitDate() != null && !filterAccountDTO.getInitDate().isEmpty()
                && !dateValidator.isValidDate(filterAccountDTO.getInitDate()))
        {
            throw new GeneralException("The start date has a not valid date format, it must be YYYY-mm-dd.");
        }
        if (filterAccountDTO.getEndDate() != null && !filterAccountDTO.getEndDate().isEmpty()
                && !dateValidator.isValidDate(filterAccountDTO.getEndDate()))
        {
            throw new GeneralException("The end date has a not valid date format, it must be YYYY-mm-dd.");
        }

        List<SelectAccountDTO> accounts = this.accountService.filterAccount(filterAccountDTO, getAuthUsername());

        if (!accounts.isEmpty())
            return new ResponseEntity<>(accounts, HttpStatus.OK);
        else
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    private Authentication getAuthentication()
    {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    private String getAuthUsername()
    {
        Authentication authentication = this.getAuthentication();

        return authentication.getName();
    }

    private Account mapDtoToEntity(CreateAccountDTO accountDTO)
    {
        Account account = new Account();

        account.setDate(dateValidator.converToDate(accountDTO.getDate()));
        account.setAmount(accountDTO.getAmount());
        account.setCategory(new Category(accountDTO.getCategoryId()));
        account.setFinancialStatement(new FinancialStatement(accountDTO.getFinancialStatementId()));
        account.setRecurring(accountDTO.isRecurring());

        return account;
    }

    private Account mapDtoToEntity(CreateAccountDTO accountDTO, Long acccountId)
    {
        Account account = this.mapDtoToEntity(accountDTO);
        account.setId(acccountId);

        return account;
    }

    private void validateAccount(CreateAccountDTO accountDTO)
    {
        if (!dateValidator.isValidDate(accountDTO.getDate()))
            throw new GeneralException("The date format is not valid, it must be 'yyyy-MM-dd'");

        BigDecimal zero = new BigDecimal(0);

        if (accountDTO.getAmount().compareTo(zero) < 0)
            throw new GeneralException("The amount for the account can not bet less than zero.");
    }
}
