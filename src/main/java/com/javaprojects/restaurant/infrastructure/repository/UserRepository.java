package com.javaprojects.restaurant.infrastructure.repository;

import com.javaprojects.restaurant.infrastructure.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<UserEntity, String> {
}
