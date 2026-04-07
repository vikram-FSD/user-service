package com.demo.user.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.user.dto.LogoutRequest;
import com.demo.user.dto.Response;
import com.demo.user.dto.UserRequest;
import com.demo.user.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Response.ResponseMessage> register(@Valid @RequestBody UserRequest userRequest) {
        authService.register(userRequest);
        Response.ResponseMessage responseMessage = new Response.ResponseMessage(LocalDateTime.now(),
                HttpStatus.CREATED.value(), "User registered successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(responseMessage);
    }

    @PostMapping("/refresh")
    public ResponseEntity<Response.ResponseData> refreshToken(HttpServletRequest request,
            HttpServletResponse response) {

        Response.ResponseData responseData = new Response.ResponseData(LocalDateTime.now(), HttpStatus.OK.value(),
                "Token refreshed successfully", authService.refreshToken(request, response));
        return ResponseEntity.status(HttpStatus.OK).body(responseData);
    }

    @PostMapping("/login")
    public ResponseEntity<Response.ResponseData> login(@Valid @RequestBody UserRequest userRequest,
            HttpServletResponse response) {
        Response.ResponseData responseData = new Response.ResponseData(LocalDateTime.now(), HttpStatus.OK.value(),
                "User logged in successfully", authService.login(userRequest, response));
        return ResponseEntity.status(HttpStatus.OK).body(responseData);
    }

    @PostMapping("/logout")
    public ResponseEntity<Response.ResponseMessage> logout(HttpServletRequest request, HttpServletResponse response,
            @Valid @RequestBody LogoutRequest logoutRequest) {
        authService.logout(request, response, logoutRequest.accessToken());
        Response.ResponseMessage responseMessage = new Response.ResponseMessage(LocalDateTime.now(),
                HttpStatus.OK.value(), "User logged out successfully");
        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }
}
