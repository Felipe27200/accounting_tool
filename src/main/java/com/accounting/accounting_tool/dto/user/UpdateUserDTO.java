package com.accounting.accounting_tool.dto.user;

import jakarta.validation.constraints.NotBlank;

public class UpdateUserDTO
{
    @NotBlank(message = "The new name is required")
    private String name;

    public UpdateUserDTO() {
    }

    public UpdateUserDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
