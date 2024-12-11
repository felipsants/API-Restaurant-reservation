package com.javaprojects.restaurant.infrastructure.repository;

import com.javaprojects.restaurant.infrastructure.entity.ReservationEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReservationRepository extends MongoRepository<ReservationEntity, String> {

}
