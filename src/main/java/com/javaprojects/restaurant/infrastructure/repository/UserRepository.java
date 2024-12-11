package com.javaprojects.restaurant.infrastructure.repository;

import com.javaprojects.restaurant.infrastructure.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<UserEntity, String> {
    Optional<UserEntity> findByEmail(String email);
}