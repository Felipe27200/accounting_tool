package com.accounting.accounting_tool.controller;

import com.accounting.accounting_tool.common.DateFormatValidator;
import com.accounting.accounting_tool.dto.account.CreateAccountDTO;
import com.accounting.accounting_tool.entity.Account;
import com.accounting.accounting_tool.entity.Category;
import com.accounting.accounting_tool.entity.FinancialStatement;
import com.accounting.accounting_tool.error_handling.exception.GeneralException;
import com.accounting.accounting_tool.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping(path = "${apiPrefix}/accounts")
public class AccountController
{
    private final AccountService accountService;
    private final DateFormatValidator dateValidator;

    @Autowired
    public AccountController(
            AccountService accountService,
            DateFormatValidator dateValidator
    ) {
        this.accountService = accountService;
        this.dateValidator = dateValidator;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createAccount(@Valid @RequestBody CreateAccountDTO accountDTO)
    {
        if (!dateValidator.isValidDate(accountDTO.getDate()))
            throw new GeneralException("The date format is not valid, it must be 'yyyy-MM-dd'");

        BigDecimal zero = new BigDecimal(0);

        if (accountDTO.getAmount().compareTo(zero) < 0)
            throw new GeneralException("The amount for the account can not bet less than zero.");

        Account account = new Account();

        account.setDate(dateValidator.converToDate(accountDTO.getDate()));
        account.setAmount(accountDTO.getAmount());
        account.setCategory(new Category(accountDTO.getCategoryId()));
        account.setFinancialStatement(new FinancialStatement(accountDTO.getFinancialStatementId()));
        account.setRecurring(accountDTO.isRecurring());

        return new ResponseEntity<>(this.accountService.save(account, getAuthUsername()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id)
    {
        return new ResponseEntity<>(this.accountService.findByIdAndUser(id, getAuthUsername()), HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllByUser()
    {
        return new ResponseEntity<>(this.accountService.findAllByUser(getAuthUsername()), HttpStatus.OK);
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
}
