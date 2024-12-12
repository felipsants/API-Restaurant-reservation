package com.javaprojects.restaurant.infrastructure.repository;

import com.javaprojects.restaurant.infrastructure.entity.ReservationEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ReservationRepository extends MongoRepository<ReservationEntity, String> {
    Optional<ReservationEntity> findByUserId(String userId);
}
