package com.accounting.accounting_tool.controller;

import com.accounting.accounting_tool.common.DateFormatValidator;
import com.accounting.accounting_tool.dto.financial_statement.CreateFinancialStatementDTO;
import com.accounting.accounting_tool.entity.FinancialStatement;
import com.accounting.accounting_tool.entity.User;
import com.accounting.accounting_tool.error_handling.exception.GeneralException;
import com.accounting.accounting_tool.service.FinancialStatementService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "${apiPrefix}/financial-statement")
public class FinancialStatementController
{
    private FinancialStatementService financialStatementService;
    private DateFormatValidator dateFormatValidator;

    @Autowired
    public FinancialStatementController(FinancialStatementService financialStatementService, DateFormatValidator dateFormatValidator)
    {
        this.financialStatementService = financialStatementService;
        this.dateFormatValidator = dateFormatValidator;
    }

    @PostMapping("/save")
    public ResponseEntity<?> save (@Valid @RequestBody CreateFinancialStatementDTO financialStatementDTO)
    {
        if (!dateFormatValidator.isValidDate(financialStatementDTO.getInitDate()))
            throw new GeneralException("The init date format is not valid, it must be 'yyyy-MM-dd'");

        String endDate = financialStatementDTO.getEndDate() != null ? financialStatementDTO.getEndDate() : "";
        FinancialStatement financialStatement = new FinancialStatement();

        if (!endDate.isEmpty() && !dateFormatValidator.isValidDate(endDate))
            throw new GeneralException("The end date format is not valid, it must be 'yyyy-MM-dd'");

        financialStatement.setEndDate( (endDate.isEmpty()) ? null : dateFormatValidator.converToDate(endDate));

        financialStatement.setName(financialStatementDTO.getName());
        financialStatement.setUser(new User(this.getAuthUsername()));
        financialStatement.setInitDate(dateFormatValidator.converToDate(financialStatementDTO.getInitDate()));

        FinancialStatement newFinancialStatement = this.financialStatementService.save(financialStatement);

        return new ResponseEntity<>(newFinancialStatement, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<?> findAll()
    {
        String username = this.getAuthUsername();

        List<FinancialStatement> financialStatementList = this.financialStatementService.findAllByUser(username);

        return new ResponseEntity<>(financialStatementList, HttpStatus.OK);
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
