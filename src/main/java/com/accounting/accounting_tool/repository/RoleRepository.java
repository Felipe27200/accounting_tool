package com.accounting.accounting_tool.repository;

import com.accounting.accounting_tool.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>
{
    Role findRoleByName(String name);
}
