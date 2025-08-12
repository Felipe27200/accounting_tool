package com.accounting.accounting_tool.controller;

import com.accounting.accounting_tool.entity.Role;
import com.accounting.accounting_tool.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "${apiPrefix}/roles")
public class RoleController
{
    private RoleService roleService;

    @Autowired
    public void setRoleService(RoleService roleService)
    {
        this.roleService = roleService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Role>> findAll()
    {
        return new ResponseEntity<>(roleService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Role> findById(@PathVariable Long id)
    {
        return new ResponseEntity<>(this.roleService.findById(id), HttpStatus.OK);
    }
}
