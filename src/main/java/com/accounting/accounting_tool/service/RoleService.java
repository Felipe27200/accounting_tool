package com.accounting.accounting_tool.service;

import com.accounting.accounting_tool.entity.Role;
import com.accounting.accounting_tool.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
}
