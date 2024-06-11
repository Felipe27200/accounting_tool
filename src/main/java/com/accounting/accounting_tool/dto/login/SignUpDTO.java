package com.accounting.accounting_tool.dto.login;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SignUpDTO
{
    @NotBlank(message = "The name of the user can not be empty")
    private String name;
    @NotBlank(message = "The password is required")
    @Size(min = 8, message = "The password have to have minimum 8 characters")
    private String password;
    @NotBlank(message = "The username is required")
    private String username;

    public SignUpDTO() {
    }

    public SignUpDTO(String name, String password, String username) {
        this.name = name;
        this.password = password;
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
