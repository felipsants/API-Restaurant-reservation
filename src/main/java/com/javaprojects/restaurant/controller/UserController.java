package com.javaprojects.restaurant.controller;

import com.javaprojects.restaurant.infrastructure.entity.UserEntity;
import com.javaprojects.restaurant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public UserEntity createUser(@RequestBody UserEntity user) {
        return userService.createUser(user);
    }

    @GetMapping
    public List<UserEntity> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserEntity getUserById(@PathVariable String id) {
        return userService.getUserById(id);
    }

    @GetMapping("/user/{email}")
    public UserEntity getUserByEmail(@RequestParam String email) {
        return userService.getUserByEmail(email);
    }
}
