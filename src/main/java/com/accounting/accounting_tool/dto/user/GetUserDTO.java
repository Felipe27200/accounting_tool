package com.accounting.accounting_tool.dto.user;

import com.accounting.accounting_tool.entity.Role;
import com.accounting.accounting_tool.entity.User;

import java.util.ArrayList;
import java.util.List;

public class GetUserDTO
{
    private Long id;
    private String name;
    private String username;
    private Role role;

    public GetUserDTO() {
    }

    public GetUserDTO(Long id, String name, String username) {
        this.username = username;
        this.name = name;
        this.id = id;
    }

    public GetUserDTO(Long id, String name, String username, Long role)
    {
        this(id, username, name);

        Role roleEntity = new Role();
        roleEntity.setId(role);

        this.role = roleEntity;
    }

    public GetUserDTO(Long id, String name, String username, Role role) {
        this(id, name, username);
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

}
