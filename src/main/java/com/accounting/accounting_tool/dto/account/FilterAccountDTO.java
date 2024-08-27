package com.accounting.accounting_tool.dto.account;

import java.util.Date;

public class FilterAccountDTO
{
    String initDate;
    String endDate;
    Long statementId;
    Long categoryId;

    public FilterAccountDTO() {
    }

    public FilterAccountDTO(String initDate, String endDate, Long statementId, Long categoryId) {
        this.initDate = initDate;
        this.endDate = endDate;
        this.statementId = statementId;
        this.categoryId = categoryId;
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

    public Long getStatementId() {
        return statementId;
    }

    public void setStatementId(Long statementId) {
        this.statementId = statementId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
