package com.accounting.accounting_tool.dto.account;

import com.accounting.accounting_tool.dto.category.CategoryForAccountDTO;
import com.accounting.accounting_tool.dto.financial_statement.FinancialStatementForAccountDTO;
import com.accounting.accounting_tool.dto.user.GetUserDTO;

import java.math.BigDecimal;
import java.util.Date;

public class SelectAccountDTO
{
    private Long id;
    private BigDecimal amount;
    private Date date;
    private boolean isRecurring;

    private CategoryForAccountDTO categoryDTO;
    private FinancialStatementForAccountDTO financialStatement;
    private GetUserDTO user;

    public SelectAccountDTO() {
    }

    public SelectAccountDTO(Long id, BigDecimal amount, Date date, boolean isRecurring, CategoryForAccountDTO categoryDTO, FinancialStatementForAccountDTO financialStatement, GetUserDTO user) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.isRecurring = isRecurring;
        this.categoryDTO = categoryDTO;
        this.financialStatement = financialStatement;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isRecurring() {
        return isRecurring;
    }

    public void setRecurring(boolean recurring) {
        isRecurring = recurring;
    }

    public CategoryForAccountDTO getCategoryDTO() {
        return categoryDTO;
    }

    public void setCategoryDTO(CategoryForAccountDTO categoryDTO) {
        this.categoryDTO = categoryDTO;
    }

    public FinancialStatementForAccountDTO getFinancialStatement() {
        return financialStatement;
    }

    public void setFinancialStatement(FinancialStatementForAccountDTO financialStatement) {
        this.financialStatement = financialStatement;
    }

    public GetUserDTO getUser() {
        return user;
    }

    public void setUser(GetUserDTO user) {
        this.user = user;
    }
}
