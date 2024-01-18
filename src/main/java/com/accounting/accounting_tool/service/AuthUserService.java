package com.accounting.accounting_tool.service;

import com.accounting.accounting_tool.entity.Role;
import com.accounting.accounting_tool.entity.User;
import com.accounting.accounting_tool.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
/*
* UserDetailService is used by spring security for retrieving the
* username, password and other attributes for authenticating the user.
* */
public class AuthUserService implements UserDetailsService
{
    private UserRepository userRepository;
    private RoleService roleService;

    @Autowired
    public AuthUserService(UserRepository userRepository, RoleService roleService)
    {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        User user = this.userRepository.findUserByUsername(username);

        if (user == null)
            throw new UsernameNotFoundException("User don't find");

        String roleName = user.getRole().getName();
        Set<GrantedAuthority> authority = Collections.singleton(new SimpleGrantedAuthority(roleName));

        return new org.springframework.security.core
            .userdetails.User(username, user.getPassword(), authority);
    }

    public User createUser(User user, String roleName)
    {
        Role role = this.roleService.findRoleByName(roleName);

        user.setRole(role);

        return this.userRepository.save(user);
    }

}
