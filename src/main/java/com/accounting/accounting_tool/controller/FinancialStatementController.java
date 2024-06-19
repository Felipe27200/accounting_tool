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

import java.util.Date;
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
    	FinancialStatement financialStatement = this.convertToFinancialStatement(financialStatementDTO);
        FinancialStatement newFinancialStatement = this.financialStatementService.save(financialStatement);

        return new ResponseEntity<>(newFinancialStatement, HttpStatus.OK);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody CreateFinancialStatementDTO createFinancialStatementDTO, @PathVariable Long id)
    {
    	FinancialStatement financialStatement = this.convertToFinancialStatement(createFinancialStatementDTO);
        financialStatement.setId(id);

    	FinancialStatement modifiedFinancial = this.financialStatementService.update(financialStatement);
    	
    	return new ResponseEntity<>(modifiedFinancial, HttpStatus.OK);
    }
    
    @GetMapping("/")
    public ResponseEntity<?> findAll()
    {
        String username = this.getAuthUsername();

        List<FinancialStatement> financialStatementList = this.financialStatementService.findAllByUser(username);

        return new ResponseEntity<>(financialStatementList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id)
    {
    	String username = this.getAuthUsername();
    	
    	FinancialStatement financialStatement = this.financialStatementService.findByIdAndUser(id, username);
    	
    	return new ResponseEntity<>(financialStatement, HttpStatus.OK);
    }

    @GetMapping("/{name}/name")
    public ResponseEntity<?> findByName(@PathVariable String name)
    {
    	String username = this.getAuthUsername();
    	
    	List<FinancialStatement> financialStatement = this.financialStatementService.findByNameAndUser(name, username);
    	
    	return new ResponseEntity<>(financialStatement, HttpStatus.OK);
    }

    @GetMapping("search/{name}")
    public ResponseEntity<?> findByNameCoincidence(@PathVariable String name)
    {
    	String username = this.getAuthUsername();
    	
    	List<FinancialStatement> financialStatement = this.financialStatementService.findByNameCoincidenceAndUser(name, username);
    	
    	return new ResponseEntity<>(financialStatement, HttpStatus.OK);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id)
    {
    	String message = this.financialStatementService.deleteById(id, getAuthUsername());
    	
    	return new ResponseEntity<>(message, HttpStatus.OK);
    }
    
    private FinancialStatement convertToFinancialStatement(CreateFinancialStatementDTO financialStatementDTO)
    {
    	if (!dateFormatValidator.isValidDate(financialStatementDTO.getInitDate()))
            throw new GeneralException("The init date format is not valid, it must be 'yyyy-MM-dd'");

        String endDate = financialStatementDTO.getEndDate() != null ? financialStatementDTO.getEndDate() : "";
        FinancialStatement financialStatement = new FinancialStatement();

        if (!endDate.isEmpty())
        	this.validateEndDate(financialStatementDTO.getInitDate(), endDate);
        
        financialStatement.setEndDate( (endDate.isEmpty()) ? null : dateFormatValidator.converToDate(endDate));
        financialStatement.setInitDate(dateFormatValidator.converToDate(financialStatementDTO.getInitDate()));
        financialStatement.setName(financialStatementDTO.getName());
        financialStatement.setUser(new User(this.getAuthUsername()));
        
        return financialStatement;
    }

    private FinancialStatement convertToFinancialStatement(CreateFinancialStatementDTO financialStatementDTO, Long id)
    {
    	FinancialStatement financialStatement = this.convertToFinancialStatement(financialStatementDTO);
    	financialStatement.setId(id);
    	
    	return financialStatement;
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
    
    private void validateEndDate(String initDate, String endDate)
    {
    	if (!dateFormatValidator.isValidDate(endDate))
    		throw new GeneralException("The end date format is not valid, it must be 'yyyy-MM-dd'");
    	
    	if (!this.isGreaterDate(initDate, endDate))
    		throw new GeneralException("The end date is greater than the init date.");    	
    }
    
    private boolean isGreaterDate(String strDate1, String strDate2)
    {
    	Date date1 = dateFormatValidator.converToDate(strDate1);
    	Date date2 = dateFormatValidator.converToDate(strDate2);
    	
    	if (date1.after(date2))
    		return false;
    	else
    		return true;
    }
}
