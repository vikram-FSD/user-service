package com.demo.user.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.user.dto.Response;
import com.demo.user.service.UserService;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<Response.ResponseData> getAllUsers(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Response.ResponseData responseData = new Response.ResponseData(LocalDateTime.now(), HttpStatus.OK.value(),
                "Users fetched successfully", userService.getAllUsers(page, size));

        return ResponseEntity.status(HttpStatus.OK).body(responseData);
    }

}
