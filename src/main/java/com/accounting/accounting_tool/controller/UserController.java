package com.accounting.accounting_tool.controller;

import com.accounting.accounting_tool.dto.user.GetUserDTO;
import com.accounting.accounting_tool.entity.User;
import com.accounting.accounting_tool.service.UserService;
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
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update (@RequestBody User userModified, @PathVariable Long id)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        userModified.setUsername(authentication.getName());

        return new ResponseEntity<>(userModified, HttpStatus.OK);
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
        String message = this.userService.deleteUser(id);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    private List<GetUserDTO> convertListUserToGEtDTO(List<User> userList)
    {
        return userList
                // The stream() let create a stream of the Collection
                // in this case a list of User entity
                .stream()
                // The map() method let us make a stream
                // based on the lambda in its parenthesis

                /*
                * In this lambda we are iterating through the userList
                * and convert his elements to GetUserDTO
                * */
                .map((user) -> modelMapper.map(user, GetUserDTO.class))
                .collect(Collectors.toList());
    }
}
