package com.javaprojects.restaurant.controller;

import com.javaprojects.restaurant.infrastructure.entity.UserEntity;
import com.javaprojects.restaurant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public UserEntity createUser(@RequestBody UserEntity user) {
        return userService.createUser(user);
    }

    @GetMapping("/{id}")
    public UserEntity getUserById(@PathVariable String id) {
        return userService.getUserById(id);
    }

    @GetMapping
    public UserEntity getUserByEmail(@RequestParam String email) {
        return userService.getUserByEmail(email);
    }
}
