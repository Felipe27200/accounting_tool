package com.accounting.accounting_tool.dto.account;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class CreateAccountDTO
{
    @NotBlank(message = "The date field is required.")
    private String date;
    @Min(value = 1, message = "The account catalogue is required")
    @NotNull(message = "The account catalogue is required")
    private BigDecimal amount;
    @NotNull(message = "The is-recurring field is required")
    private boolean isRecurring;
    @Min(value = 1, message = "The category is required")
    @NotNull(message = "The category is required")
    private Long categoryId;
    @Min(value = 1, message = "The financial statement is required")
    @NotNull(message = "The financial statement is required")
    private Long financialStatementId;

    public CreateAccountDTO() {
    }

    public CreateAccountDTO(String date, BigDecimal amount, boolean isRecurring, Long categoryId, Long financialStatementId) {
        this.date = date;
        this.amount = amount;
        this.isRecurring = isRecurring;
        this.categoryId = categoryId;
        this.financialStatementId = financialStatementId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public boolean isRecurring() {
        return isRecurring;
    }

    public void setRecurring(boolean recurring) {
        isRecurring = recurring;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getFinancialStatementId() {
        return financialStatementId;
    }

    public void setFinancialStatementId(Long financialStatementId) {
        this.financialStatementId = financialStatementId;
    }
}
