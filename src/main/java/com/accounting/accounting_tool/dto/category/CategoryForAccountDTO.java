package com.accounting.accounting_tool.dto.category;

import com.accounting.accounting_tool.entity.AccountCatalogue;

public class CategoryForAccountDTO
{
    private Long id;
    private Long parentCategory;
    private String name;
    private AccountCatalogue accountCatalogue;

    public CategoryForAccountDTO() {
    }

    public CategoryForAccountDTO(Long id, Long parentCategory, String name, AccountCatalogue accountCatalogue)
    {
        this.id = id;
        this.parentCategory = parentCategory;
        this.name = name;
        this.accountCatalogue = accountCatalogue;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(Long parentCategory) {
        this.parentCategory = parentCategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AccountCatalogue getAccountCatalogue() {
        return accountCatalogue;
    }

    public void setAccountCatalogue(AccountCatalogue accountCatalogue) {
        this.accountCatalogue = accountCatalogue;
    }
}