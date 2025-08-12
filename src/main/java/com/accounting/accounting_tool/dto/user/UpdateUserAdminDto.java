package com.accounting.accounting_tool.dto.user;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class UpdateUserAdminDto
{
    private String username;
    private String password;
    @NotBlank(message = "The name of the user can not be empty")
    private String name;

    @Min(value = 1, message = "The account catalogue is required")
    private Long idRole;

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

    public UpdateUserAdminDto(String username, String password, String name, Long idRole) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.idRole = idRole;
    }

    public Long getIdRole() {
        return idRole;
    }

    public void setIdRole(Long idRole) {
        this.idRole = idRole;
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
