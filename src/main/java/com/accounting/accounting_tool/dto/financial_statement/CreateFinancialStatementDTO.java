package com.accounting.accounting_tool.dto.financial_statement;

import jakarta.validation.constraints.NotBlank;

public class CreateFinancialStatementDTO
{
    @NotBlank(message = "The name is required")
    private String name;
    @NotBlank(message = "The init date is required")
    private String initDate;
    private String endDate;

    public CreateFinancialStatementDTO() {
    }

    public CreateFinancialStatementDTO(String name, String initDate) {
        this.name = name;
        this.initDate = initDate;
    }

    public CreateFinancialStatementDTO(String name, String initDate, String endDate) {
        this.name = name;
        this.initDate = initDate;
        this.endDate = endDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInitDate() {
        return initDate;
    }

    public void setInitDate(String initDate) {
        this.initDate = initDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
