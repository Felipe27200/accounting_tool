package com.accounting.accounting_tool.service;

import com.accounting.accounting_tool.common.DateFormatValidator;
import com.accounting.accounting_tool.dto.account.FilterAccountDTO;
import com.accounting.accounting_tool.dto.account.SelectAccountDTO;
import com.accounting.accounting_tool.entity.Account;
import com.accounting.accounting_tool.entity.Category;
import com.accounting.accounting_tool.entity.FinancialStatement;
import com.accounting.accounting_tool.entity.User;
import com.accounting.accounting_tool.error_handling.exception.GeneralException;
import com.accounting.accounting_tool.error_handling.exception.NotFoundException;
import com.accounting.accounting_tool.repository.AccountRepository;
import com.accounting.accounting_tool.repository.CustomizedAccountRepositoryImpl;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class AccountService
{
    private final AccountRepository accountRepository;
    private final FinancialStatementService financialStatementService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final DateFormatValidator dateFormat;
    private final CustomizedAccountRepositoryImpl customizedAccountRepository;

    @Autowired
    public AccountService(
        AccountRepository accountRepository,
        FinancialStatementService financialStatementService,
        CategoryService categoryService,
        UserService userService,
        DateFormatValidator dateFormat,
        CustomizedAccountRepositoryImpl customizedAccountRepository
    ) {
        this.accountRepository = accountRepository;
        this.financialStatementService = financialStatementService;
        this.categoryService = categoryService;
        this.userService = userService;
        this.dateFormat = dateFormat;
        this.customizedAccountRepository = customizedAccountRepository;
    }

    @Transactional
    public Account save(Account account, String username)
    {
        User user = this.userService.findByUsername(username);
        FinancialStatement financialStatement =
            this.financialStatementService
                .findByIdAndUser(
                    account.getFinancialStatement().getId(),
                    user.getUsername()
                );

        this.validateAccount(financialStatement, account);

        account.setCategory(this.getCategory(account, user));
        account.setFinancialStatement(financialStatement);

        return this.accountRepository.save(account);
    }

    @Transactional
    public SelectAccountDTO update(Account account, String username)
    {
        User user = userService.findByUsername(username);
        FinancialStatement financialStatement =
                this.financialStatementService
                        .findByIdAndUser(
                                account.getFinancialStatement().getId(),
                                user.getUsername()
                        );

        this.findByIdAndUser(account.getId(), user.getUsername());
        this.validateAccount(financialStatement, account);

        account.setCategory(this.getCategory(account, user));
        account.setFinancialStatement(financialStatement);

        Account accountUpdated = this.accountRepository.save(account);

        return this.accountRepository.findAccountById(accountUpdated.getId());
    }

    public List<SelectAccountDTO> filterAccount(FilterAccountDTO filterAccountDTO, String username)
    {
    	User user = this.userService.findByUsername(username);
    	
        return this.customizedAccountRepository.filterAccounts(filterAccountDTO, user.getUsername());
    }

    public SelectAccountDTO findByIdAndUser(Long id, String username)
    {
        User user = this.userService.findByUsername(username);
        SelectAccountDTO account = this.accountRepository.findAccountByIdAndUser(id, user.getId());

        if (account == null)
            throw new NotFoundException("The account with the id: " + id + " was not found.");

        return account;
    }

    public List<SelectAccountDTO> findByDateAndUser(Date date, String username)
    {
        User user = this.userService.findByUsername(username);
        List<SelectAccountDTO> account = this.accountRepository.findByDateAndUser(date, user.getId());

        if (account == null || account.size() <= 0)
        {
            SimpleDateFormat formatter = (new SimpleDateFormat("yyyy-MM-dd"));
            String format = formatter.format(date);

            throw new NotFoundException("Accounts with date: " + format + " were not found.");
        }

        return account;
    }

    public List<SelectAccountDTO> findByDateRangeAndUser(String startDate, String endDate, String username)
    {
        User user = this.userService.findByUsername(username);
        List<SelectAccountDTO> accounts = this.accountRepository.findByDatRageAndUser(startDate, endDate, user.getId());

        return accounts;
    }

    public List<SelectAccountDTO> findAllByUser(String username)
    {
        User user = this.userService.findByUsername(username);
        List<SelectAccountDTO> accounts = this.accountRepository.findAllByUser(user.getId());

        return accounts;
    }

    @Transactional
    public String deleteById(Long id,String username)
    {
        SelectAccountDTO accountDto = this.findByIdAndUser(id, username);

        this.accountRepository.deleteById(accountDto.getId());

        return "Account with id: " + id + " was deleted";
    }

    private void validateAccount(FinancialStatement financialStatement, Account account)
    {
        if (financialStatement.getEndDate() != null
                && !dateFormat.isDateInRange(account.getDate(), financialStatement.getInitDate(), financialStatement.getEndDate()))
        {
            throw new GeneralException("The date is outside of the range of the financial statement");
        }
        else if (!dateFormat.isGreaterDate(account.getDate(), financialStatement.getInitDate())
                    && !(account.getDate().compareTo(financialStatement.getInitDate()) >= 0))
        {
            throw new GeneralException("The date is outside of the range of the financial statement");
        }

        this.isAmountValid(account.getAmount());
    }

    private void isAmountValid(BigDecimal amount)
    {
        BigDecimal zero = new BigDecimal(0);

        if (amount.compareTo(zero) < 0)
            throw new GeneralException("The amount for the account can not bet less than zero.");

    }

    private Category getCategory(Account account, User user)
    {
        return this.categoryService.findById(account.getCategory().getId(), user.getUsername());
    }
}
