package com.accounting.accounting_tool.service;

import com.accounting.accounting_tool.entity.Role;
import com.accounting.accounting_tool.entity.User;
import com.accounting.accounting_tool.error_handling.exception.DuplicateRecordException;
import com.accounting.accounting_tool.error_handling.exception.NotFoundException;
import com.accounting.accounting_tool.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService
{
    private UserRepository userRepository;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService (UserRepository userRepository, PasswordEncoder passwordEncoder, RoleService roleService)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    @Transactional
    public User update (User userModified)
    {
        User oldUser = this.findById(userModified.getId());
        User username = this.userRepository.findUserByUsername(userModified.getUsername());
        Role newRole = this.roleService.findById(userModified.getRole().getId());

        if (username != null
            && !username.getId().equals(oldUser.getId())
            && username.getUsername().equals(oldUser.getName())
        ) {
            throw new DuplicateRecordException("The username '"
                + userModified.getUsername()
                + " belongs to other User.");
        }

        oldUser.setName(userModified.getName());
        oldUser.setRole(newRole);

        return this.userRepository.save(oldUser);
    }

    public List<User> findAll ()
    {
        return this.userRepository.findAll();
    }

    public User findById (Long id)
    {
        Optional<User> user = this.userRepository.findById(id);

        if (user.isEmpty())
            throw new NotFoundException("The user with the id: " + id + " was not  found.");

        return user.get();
    }

    public User findByUsername (String name)
    {
        User user = this.userRepository.findUserByUsername(name);

        if (user == null)
            throw new NotFoundException("The user with the username: " + name + " was not  found.");

        return user;
    }

    public List<User> findByUsernameCoincidence(String name)
    {
        List<User> userList = this.userRepository.findUserByUsernameCoincidence(name);

        return userList;
    }

    @Transactional
    public String deleteUser(Long id)
    {
        User oldUser = this.findById(id);

        this.userRepository.deleteById(oldUser.getId());

        return "The user: " + oldUser.getName() + " was deleted";
    }

    @Transactional
    public String changePassword(String oldPassword, String newPassword, String passwordConfirmation, String username) throws Exception
    {
        if (!newPassword.equals(passwordConfirmation))
            throw new Exception("The new password and the Confirm Password are not the same.");

        User user = this.findByUsername(username);

        /*
        * The matches() method is useful to compare text not encrypted and encrypted
        * and verify if the both make match.
        * */
        if (!this.passwordEncoder.matches(oldPassword, user.getPassword()))
            throw new Exception("The current password is not that.");

        user.setPassword(this.passwordEncoder.encode(newPassword));
        this.userRepository.save(user);

        return "Password updated successfully";
    }

    @Transactional
    public String changePasswordAdmin(String newPassword, String passwordConfirmation, Long id) throws Exception
    {
        if (!newPassword.equals(passwordConfirmation))
            throw new Exception("The new password and the Confirm Password are not the same.");

        User user = this.findById(id);

        user.setPassword(this.passwordEncoder.encode(newPassword));
        this.userRepository.save(user);

        return "Password updated successfully";
    }

}
