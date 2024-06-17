package com.accounting.accounting_tool.service;

import com.accounting.accounting_tool.entity.FinancialStatement;
import com.accounting.accounting_tool.entity.User;
import com.accounting.accounting_tool.error_handling.exception.NotFoundException;
import com.accounting.accounting_tool.repository.FinancialStatementRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FinancialStatementService
{
    private final FinancialStatementRepository financialStatementRepository;
    private final UserService userService;

    @Autowired
    public FinancialStatementService(FinancialStatementRepository financialStatementRepository, UserService userService) {
        this.financialStatementRepository = financialStatementRepository;
        this.userService = userService;
    }

    @Transactional
    public FinancialStatement save(FinancialStatement newFinancialStatement)
    {
        User user = this.userService.findByUsername(newFinancialStatement.getUser().getUsername());
        newFinancialStatement.setUser(user);

        FinancialStatement financialStatement = this.financialStatementRepository.save(newFinancialStatement);

        return this.financialStatementRepository.findByIdAndUser(financialStatement.getId(), user.getId());
    }

    public List<FinancialStatement> findAllByUser(String username)
    {
        User user = this.userService.findByUsername(username);

        return this.financialStatementRepository.findByUsername(username);
    }

    public FinancialStatement findByIdAndUser(Long id, String username)
    {
        User user = this.userService.findByUsername(username);
        FinancialStatement financialStatement = this.financialStatementRepository.findByIdAndUser(id, user.getId());

        if (financialStatement == null)
            throw new NotFoundException("The financial statement with the de id: " + id + " not found.");

        return financialStatement;
    }

    public FinancialStatement findByNameAndUser(String name, String username)
    {
        User user = this.userService.findByUsername(username);
        FinancialStatement financialStatement = this.financialStatementRepository.findByNameAndUser(name, user.getId());

        if (financialStatement == null)
            throw new NotFoundException("The financial statement with the de id: " + name + " not found.");

        return financialStatement;
    }

    public List<FinancialStatement> findByNameCoincidenceAndUser(String name, String username)
    {
        User user = this.userService.findByUsername(username);

        return this.financialStatementRepository
                .findByNameCoincidenceAndUser(name, user.getId());
    }
}
