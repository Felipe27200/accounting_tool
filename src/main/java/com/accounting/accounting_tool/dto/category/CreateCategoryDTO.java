package com.accounting.accounting_tool.dto.category;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateCategoryDTO
{
    @NotBlank(message = "The category name is required")
    private String name;

    /*
    * The validation with @NotBlank can not be applied to numbers.
    * */
    @Min(value = 1, message = "The account catalogue is required")
    @NotNull(message = "The account catalogue is required")
    private Long accountCatalogueId;
    private Long parentCategory;

    public CreateCategoryDTO() {
    }

    public CreateCategoryDTO(String name, Long accountCatalogueId, Long parentCategory) {
        this.name = name;
        this.accountCatalogueId = accountCatalogueId;
        this.parentCategory = parentCategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getAccountCatalogueId() {
        return accountCatalogueId;
    }

    public void setAccountCatalogueId(Long accountCatalogueId) {
        this.accountCatalogueId = accountCatalogueId;
    }

    public Long getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(Long parentCategory) {
        this.parentCategory = parentCategory;
    }
}
