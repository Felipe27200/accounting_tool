package com.accounting.accounting_tool.service;

import com.accounting.accounting_tool.entity.Role;
import com.accounting.accounting_tool.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService
{
    private RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository)
    {
        this.roleRepository = roleRepository;
    }

    public Role findRoleByName(String name)
    {
        return this.roleRepository.findRoleByName(name);
    }

    public List<Role> findAll()
    {
        return this.roleRepository.findAll();
    }

    public Role findById(Long id)
    {
        if (id == null || id <= 0L)
            throw new IllegalArgumentException("Invalid ID supplied");

        Role role = this.roleRepository.findRoleById(id);

        if (role == null)
            throw new IllegalArgumentException("Role with ID " + id + " not found");

        return role;
    }
}
