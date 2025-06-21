package com.accounting.accounting_tool.controller;

import com.accounting.accounting_tool.dto.login.ChangePasswordAdminDTO;
import com.accounting.accounting_tool.dto.login.ChangePasswordDTO;
import com.accounting.accounting_tool.dto.user.GetUserDTO;
import com.accounting.accounting_tool.dto.user.UpdateUserAdminDto;
import com.accounting.accounting_tool.dto.user.UpdateUserDTO;
import com.accounting.accounting_tool.entity.User;
import com.accounting.accounting_tool.error_handling.exception.GeneralException;
import com.accounting.accounting_tool.response.BasicResponse;
import com.accounting.accounting_tool.service.UserService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "${apiPrefix}/users")
public class UserController
{
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserController(
        UserService userService,
        ModelMapper modelMapper
    ) {
        this.userService = userService;
        this.modelMapper = modelMapper;

        /*
        * This is used to define explicit mappings
        * between source and destination properties
        * */
        this.modelMapper.typeMap(User.class, GetUserDTO.class)
            .addMapping(User::getUsername, GetUserDTO::setUserName);
    }

    @PutMapping("/update")
    public ResponseEntity<?> update (@Valid @RequestBody UpdateUserDTO newName)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = this.userService.findByUsername(authentication.getName());
        user.setName(newName.getName());

        GetUserDTO userDTO = this.modelMapper.map(this.userService.update(user), GetUserDTO.class);

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PutMapping("/update-user/{id}")
    public ResponseEntity<GetUserDTO> updateUser (@Valid @RequestBody UpdateUserAdminDto updatedUser, @PathVariable("id") Long userId)
    {
        if (userId <= 0)
            throw new GeneralException("The user id must be greater than 0");

        User user = this.userService.findById(userId);

        user.setName(updatedUser.getName());

        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty())
        {
            if (updatedUser.getPassword().length() < 8)
                throw new GeneralException("The password must be at least 8 characters");

            user.setPassword(updatedUser.getPassword());
        }

        GetUserDTO userDTO = this.modelMapper.map(this.userService.update(user), GetUserDTO.class);

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PutMapping("/change-password")
    public ResponseEntity<BasicResponse<String>> changePassword (@Valid @RequestBody ChangePasswordDTO changePasswordDTO) throws Exception
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String message = this.userService.changePassword(
            changePasswordDTO.getOldPassword(),
            changePasswordDTO.getNewPassword(),
            changePasswordDTO.getPasswordConfirmation(),
            authentication.getName()
        );

        BasicResponse<String> response = new BasicResponse<>(message, "successful");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/change-password-admin/{userId}")
    public ResponseEntity<BasicResponse<String>> changePasswordAdmin (@Valid @RequestBody ChangePasswordAdminDTO changePasswordDTO, @PathVariable Long userId) throws Exception
    {
        if (userId <= 0)
            throw new GeneralException("The user id must be greater than 0");

        String message = this.userService.changePasswordAdmin(
                changePasswordDTO.getNewPassword(),
                changePasswordDTO.getPasswordConfirmation(),
                userId
        );

        BasicResponse<String> response = new BasicResponse<>(message, "successful");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id)
    {
        User user = this.userService.findById(id);
        GetUserDTO userDTO = this.modelMapper.map(user, GetUserDTO.class);

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping("/search-username-coincidence/{name}")
    public ResponseEntity<?> findByUsernameCoincidence(@PathVariable String name)
    {
        List<User> userList = this.userService.findByUsernameCoincidence(name);
        List<GetUserDTO> userDTOList = this.convertListUserToGEtDTO(userList);

        return new ResponseEntity<>(userDTOList, HttpStatus.OK);
    }

    @GetMapping("/search-username/{name}")
    public ResponseEntity<?> findByUsername(@PathVariable String name)
    {
        User user = this.userService.findByUsername(name);
        GetUserDTO userDTO = this.modelMapper.map(user, GetUserDTO.class);

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<?> findAll()
    {
        List<User> userList = this.userService.findAll();

        if (userList != null)
        {
            return new ResponseEntity<>(this.convertListUserToGEtDTO(userList), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>("Empty users", HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id)
    {
        User user = this.userService.findById(id);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = this.userService.findByUsername(authentication.getName());

        if (!currentUser.getRole().getName().equals("ROLE_ADMIN")
            && !currentUser.getId().equals(user.getId())
        ) {
            throw new GeneralException("You are not allowed to delete this user");
        }

        String message = this.userService.deleteUser(id);
        BasicResponse<String> response = new BasicResponse<>(message, "successful");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private List<GetUserDTO> convertListUserToGEtDTO(List<User> userList)
    {
        return userList
                // The stream() lets us create a stream of the Collection,
                // in this case a list of User entity
                .stream()
                // The map() method lets us make a stream
                // based on the lambda in its parenthesis

                /*
                * In this lambda we are iterating through the userList
                * and convert his elements to GetUserDTO
                * */
                .map((user) -> modelMapper.map(user, GetUserDTO.class))
                .collect(Collectors.toList());
    }
}
