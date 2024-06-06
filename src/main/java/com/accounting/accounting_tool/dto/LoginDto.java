package com.accounting.accounting_tool.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginDto
{
    @NotBlank(message = "The username is required")
    private String username;
    @NotBlank(message = "The password is required")
    private String password;

    public LoginDto() {
    }

    public LoginDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername()
    {
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
}
