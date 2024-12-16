package com.javaprojects.restaurant.service;

import com.javaprojects.restaurant.infrastructure.entity.UserEntity;
import com.javaprojects.restaurant.infrastructure.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserEntity createUser(UserEntity user) {
        return userRepository.save(user);
    }
    public UserEntity getUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public UserEntity getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public UserEntity updateUser(String id,UserEntity updatedUser) {
        UserEntity existingUser = getUserById(id);

        if(updatedUser.getName() != null){
            existingUser.setName(updatedUser.getName());
        }

        if(updatedUser.getEmail() != null){
            existingUser.setEmail(updatedUser.getEmail());
        }

        if(updatedUser.getPhoneNumber() != null){
            existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
        }

        return userRepository.save(existingUser);
    }
}
