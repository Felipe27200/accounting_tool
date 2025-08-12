package com.accounting.accounting_tool.service;

import com.accounting.accounting_tool.entity.FinancialStatement;
import com.accounting.accounting_tool.entity.User;
import com.accounting.accounting_tool.error_handling.exception.NotFoundException;
import com.accounting.accounting_tool.repository.FinancialStatementRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class FinancialStatementService
{
    private final FinancialStatementRepository financialStatementRepository;
    private final AccountService accountService;
    private final UserService userService;

    @Autowired
    public FinancialStatementService(FinancialStatementRepository financialStatementRepository, UserService userService, AccountService accountService) {
        this.financialStatementRepository = financialStatementRepository;
        this.accountService = accountService;
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
    
    @Transactional
    public FinancialStatement update(FinancialStatement financialStatement)
    {
    	User user = this.userService.findByUsername(financialStatement.getUser().getUsername());
    	this.findByIdAndUser(financialStatement.getId(), user.getUsername());

    	financialStatement.setUser(user);
    	
    	FinancialStatement result = this.financialStatementRepository.save(financialStatement);
    	
    	return this.findByIdAndUser(result.getId(), user.getUsername());
    	
    }

    public List<FinancialStatement> findAllByUser(String username)
    {
        User user = this.userService.findByUsername(username);

        return this.financialStatementRepository.findByUsername(user.getUsername());
    }

    public FinancialStatement findByIdAndUser(Long id, String username)
    {
        User user = this.userService.findByUsername(username);
        FinancialStatement financialStatement = this.financialStatementRepository.findByIdAndUser(id, user.getId());

        if (financialStatement == null)
            throw new NotFoundException("The financial statement with the id: " + id + " was not found.");

        return financialStatement;
    }

    public List<FinancialStatement> findByNameAndUser(String name, String username)
    {
        User user = this.userService.findByUsername(username);
        List<FinancialStatement> financialStatement = this.financialStatementRepository.findByNameAndUser(name, user.getId());

        if (financialStatement == null)
            throw new NotFoundException("The financial statement with the id: " + name + " was not found.");

        return financialStatement;
    }

    public List<FinancialStatement> findByNameCoincidenceAndUser(String name, String username)
    {
        User user = this.userService.findByUsername(username);

        return this.financialStatementRepository
                .findByNameCoincidenceAndUser(name, user.getId());
    }

    public List<FinancialStatement> findAllByDate(Date date, String username)
    {
        User user = this.userService.findByUsername(username);

        return this.financialStatementRepository.findAllByDate(date, user.getId());
    }

    public List<FinancialStatement> findAllByDateRange(Date initDate, Date endDate, String username)
    {
        User user = this.userService.findByUsername(username);

        return this.financialStatementRepository.findAllByDateRange(initDate, endDate, user.getId());
    }

    @Transactional
    public String deleteById(Long id, String username)
    {
    	User user = this.userService.findByUsername(username);
    	
    	FinancialStatement financialStatement = this.findByIdAndUser(id, user.getUsername());

    	if (financialStatement == null)
            throw new NotFoundException("The financial statement with the id: " + id + " was not found.");

        this.accountService.deleteByStatementId(financialStatement.getId());
        this.financialStatementRepository.deleteById(id);
    	
    	return "The financial statement with the id: " + id + " was deleted successfully";
    }
}
