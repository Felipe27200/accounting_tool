package com.accounting.accounting_tool.dto.login;

public class SignUPDto
{
    private String name;
    private String password;
    private String username;

    public SignUPDto() {
    }

    public SignUPDto(String name, String password, String username) {
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
