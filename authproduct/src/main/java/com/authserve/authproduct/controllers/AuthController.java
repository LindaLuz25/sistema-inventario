package com.authserve.authproduct.controllers;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.authserve.authproduct.dto.AuthCreateUserRequest;
import com.authserve.authproduct.dto.AuthCreateUserResponse;
import com.authserve.authproduct.dto.AuthLoginRequest;
import com.authserve.authproduct.dto.AuthResponse;
import com.authserve.authproduct.service.UserDetailsServiceImpl;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @PostMapping("/log-in")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthLoginRequest entity) {
        return new ResponseEntity<>(this.userDetailsService.loginUser(entity),HttpStatus.OK);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<AuthCreateUserResponse> register(@RequestBody @Valid AuthCreateUserRequest entity) throws BadRequestException {

        return new ResponseEntity<>(this.userDetailsService.createUser(entity), HttpStatus.CREATED);
    }

    
}
