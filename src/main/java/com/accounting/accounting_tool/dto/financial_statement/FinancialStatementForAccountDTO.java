package com.accounting.accounting_tool.dto.financial_statement;

import java.util.Date;

public class FinancialStatementForAccountDTO
{
    private Long id;
    private String name;
    private Date initDate;
    private Date endDate;

    public FinancialStatementForAccountDTO() {
    }

    public FinancialStatementForAccountDTO(Long id, String name, Date initDate, Date endDate) {
        this.id = id;
        this.name = name;
        this.initDate = initDate;
        this.endDate = endDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getInitDate() {
        return initDate;
    }

    public void setInitDate(Date initDate) {
        this.initDate = initDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
