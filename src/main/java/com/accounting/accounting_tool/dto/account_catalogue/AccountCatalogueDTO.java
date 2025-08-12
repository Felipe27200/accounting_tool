package com.accounting.accounting_tool.dto.account_catalogue;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AccountCatalogueDTO
{
    @NotBlank(message = "The name of Account Catalogue can not be empty.")
    private String name;
    @NotNull(message = "The type of Account Catalogue can not be empty.")
    @JsonProperty("typeAccount")
    private Integer typeAccount;

    public AccountCatalogueDTO() {
    }

    public AccountCatalogueDTO(String name, Integer typeAccount) {
        this.name = name;
        this.typeAccount = typeAccount;
    }

    public Integer typeAccount() {
        return typeAccount;
    }

    public void setTypeAccount(Integer typeAccount) {
        this.typeAccount = typeAccount;
    }

    public Integer getTypeAccount() {
        return typeAccount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
