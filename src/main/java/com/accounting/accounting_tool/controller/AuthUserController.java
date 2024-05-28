package com.accounting.accounting_tool.controller;

import com.accounting.accounting_tool.dto.LoginDto;
import com.accounting.accounting_tool.dto.SignUPDto;
import com.accounting.accounting_tool.entity.User;
import com.accounting.accounting_tool.repository.UserRepository;
import com.accounting.accounting_tool.response.BasicResponse;
import com.accounting.accounting_tool.service.AuthUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "${apiPrefix}")
public class AuthUserController
{
    private UserRepository userRepository;
    private AuthUserService authUserService;
    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AuthUserController(
        UserRepository userRepository,
        AuthUserService authUserService,
         AuthenticationManager authenticationManager,
        PasswordEncoder passwordEncoder
    )
    {
        this.userRepository = userRepository;
        this.authUserService = authUserService;
         this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUPDto signUPDto)
    {
        if (userRepository.findUserByUsername(signUPDto.getUsername()) != null)
            return new ResponseEntity<>("Username is already exist!", HttpStatus.BAD_REQUEST);

        // Creating User object
        User newUser = new User(
            signUPDto.getName(),
            passwordEncoder.encode(signUPDto.getPassword()),
            signUPDto.getUsername());

        this.authUserService.createUser(newUser, "ROLE_PERSONAL_ACCOUNTANT");

        return new ResponseEntity<>("User successful registered!!!", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public BasicResponse<?> authenticateUser(@RequestBody LoginDto loginDto)
    {
        Authentication authenticationRequest =
            UsernamePasswordAuthenticationToken.unauthenticated(loginDto.getUsername(), loginDto.getPassword());
        Authentication authenticationResponse = this.authenticationManager.authenticate(authenticationRequest);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authenticationResponse);

        SecurityContextHolder.setContext(context);

        return new BasicResponse<>(SecurityContextHolder.getContext(), "successful");
    }

    @GetMapping("/test")
    public BasicResponse<?> test()
    {
        return new BasicResponse<>("User access Successfully", "successful");
    }
}