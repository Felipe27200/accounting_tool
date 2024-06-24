package com.accounting.accounting_tool.service;

import com.accounting.accounting_tool.common.DateFormatValidator;
import com.accounting.accounting_tool.dto.account.SelectAccountDTO;
import com.accounting.accounting_tool.entity.Account;
import com.accounting.accounting_tool.entity.Category;
import com.accounting.accounting_tool.entity.FinancialStatement;
import com.accounting.accounting_tool.entity.User;
import com.accounting.accounting_tool.error_handling.exception.GeneralException;
import com.accounting.accounting_tool.error_handling.exception.NotFoundException;
import com.accounting.accounting_tool.repository.AccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.NotActiveException;
import java.math.BigDecimal;
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

    @Autowired
    public AccountService(
        AccountRepository accountRepository,
        FinancialStatementService financialStatementService,
        CategoryService categoryService,
        UserService userService,
        DateFormatValidator dateFormat
    ) {
        this.accountRepository = accountRepository;
        this.financialStatementService = financialStatementService;
        this.categoryService = categoryService;
        this.userService = userService;
        this.dateFormat = dateFormat;
    }

    @Transactional
    public Account save(Account account, String username)
    {
        BigDecimal zero = new BigDecimal(0);
        User user = this.userService.findByUsername(username);
        Category category = this.categoryService.findById(account.getCategory().getId(), user.getUsername());
        FinancialStatement financialStatement =
            this.financialStatementService
                .findByIdAndUser(
                    account.getFinancialStatement().getId(),
                    user.getUsername()
                );

        if (financialStatement.getEndDate() != null
            && !dateFormat.isDateInRange(account.getDate(), financialStatement.getInitDate(), financialStatement.getEndDate()))
        {
            throw new GeneralException("The date is outside of the range of the financial statement");
        }
        else if (!dateFormat.isGreaterDate(account.getDate(), financialStatement.getInitDate()))
            throw new GeneralException("The date is outside of the range of the financial statement");

        if (account.getAmount().compareTo(zero) < 0)
            throw new GeneralException("The amount for the account can not bet less than zero.");

        account.setCategory(category);
        account.setFinancialStatement(financialStatement);

        return this.accountRepository.save(account);
    }

    public SelectAccountDTO findByIdAndUser(Long id, String username)
    {
        User user = this.userService.findByUsername(username);
        SelectAccountDTO account = this.accountRepository.findAccountByIdAndUser(id, user.getId());

        if (account == null)
            throw new NotFoundException("The account with the id: " + id + " was not found.");

        return account;
    }

    public List<SelectAccountDTO> findAllByUser(String username)
    {
        User user = this.userService.findByUsername(username);
        List<SelectAccountDTO> accounts = this.accountRepository.findAllByUser(user.getId());

        return accounts;

    }
}
