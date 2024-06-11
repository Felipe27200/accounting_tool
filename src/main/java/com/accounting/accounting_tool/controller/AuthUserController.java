package com.accounting.accounting_tool.controller;

import com.accounting.accounting_tool.dto.login.LoginDTO;
import com.accounting.accounting_tool.dto.login.SignUpDTO;
import com.accounting.accounting_tool.entity.User;
import com.accounting.accounting_tool.repository.UserRepository;
import com.accounting.accounting_tool.response.BasicResponse;
import com.accounting.accounting_tool.service.AuthUserService;
import com.accounting.accounting_tool.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "${apiPrefix}")
public class AuthUserController
{
    private final AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private AuthUserService authUserService;
    private PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Autowired
    public AuthUserController(
        UserRepository userRepository,
        AuthUserService authUserService,
        PasswordEncoder passwordEncoder,
        TokenService tokenService,
        AuthenticationManager authenticationManager)
    {
        this.userRepository = userRepository;
        this.authUserService = authUserService;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpDTO signUPDto)
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
    public BasicResponse<?> authenticateUser(@Valid @RequestBody LoginDTO loginDto)
    {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenService.generateToken(authentication);

        return new BasicResponse<>(jwt, "successful");
    }
}
