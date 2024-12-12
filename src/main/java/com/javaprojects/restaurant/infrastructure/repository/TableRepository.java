package com.javaprojects.restaurant.infrastructure.repository;

import com.javaprojects.restaurant.infrastructure.entity.ReservationEntity;
import com.javaprojects.restaurant.infrastructure.entity.TableEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TableRepository extends MongoRepository<TableEntity, String> {
    Optional<TableEntity> findByName(String name);
    List<TableEntity> findByAvailable(Boolean available);
    List<TableEntity> findByAvailableTrueOrderByPlacesAsc();
}
