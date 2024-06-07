package com.accounting.accounting_tool.dto.account_catalogue;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AccountCatalogueDTO
{
    @NotBlank(message = "The name of Account Catalogue can not be empty.")
    private String name;
    @NotNull(message = "The type of Account Catalogue can not be empty.")
    @JsonProperty("isEarning")
    private boolean isEarning;

    public AccountCatalogueDTO() {
    }

    public AccountCatalogueDTO(String name, boolean isEarning) {
        this.name = name;
        this.isEarning = isEarning;
    }

    public boolean isEarning() {
        return isEarning;
    }

    public void setEarning(boolean earning) {
        isEarning = earning;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
