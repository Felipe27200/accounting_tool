package com.accounting.accounting_tool.dto.account;

import java.util.List;

public class FilterAccountDTO
{
    String initDate;
    String endDate;
    Long statementId;
    List<Long> categoryList;

    public FilterAccountDTO() {
    }

    public FilterAccountDTO(String initDate, String endDate, Long statementId, List<Long> categoryList) {
        this.initDate = initDate;
        this.endDate = endDate;
        this.statementId = statementId;
        this.categoryList = categoryList;
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

    public List<Long> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Long> categoryList) {
        this.categoryList = categoryList;
    }
}
