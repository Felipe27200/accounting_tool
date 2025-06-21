package com.accounting.accounting_tool.dto.user;

import jakarta.validation.constraints.NotBlank;

public class UpdateUserAdminDto
{
    @NotBlank(message = "The username is required")
    private String username;
    private String password;
    @NotBlank(message = "The name of the user can not be empty")
    private String name;

    public UpdateUserAdminDto() {
    }

    public UpdateUserAdminDto(String username, String name) {
        this.username = username;
        this.name = name;
    }

    public UpdateUserAdminDto(String username, String password, String name) {
        this.username = username;
        this.password = password;
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
