package com.accounting.accounting_tool.dto.user;

import com.accounting.accounting_tool.entity.Role;
import com.accounting.accounting_tool.entity.User;

import java.util.ArrayList;
import java.util.List;

public class GetUserDTO
{
    private Long id;
    private String name;
    private String userName;
    private Role role;

    public GetUserDTO() {
    }

    public GetUserDTO(Long id, String name, String userName) {
        this.userName = userName;
        this.name = name;
        this.id = id;
    }

    public GetUserDTO(Long id, String name, String userName, Long role)
    {
        this(id, userName, name);

        Role roleEntity = new Role();
        roleEntity.setId(role);

        this.role = roleEntity;
    }

    public GetUserDTO(Long id, String name, String userName, Role role) {
        this(id, name, userName);
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

}
