package com.javaprojects.restaurant.service;

import com.javaprojects.restaurant.infrastructure.repository.ReservationRepository;
import com.javaprojects.restaurant.infrastructure.entity.ReservationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;

    public ReservationEntity createReservation(ReservationEntity reservation) {
        return reservationRepository.save(reservation);
    }

    public ReservationEntity findReservationById(String id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User Not Found"));
    }

    public ReservationEntity findReservationByEmail(String email) {
        return reservationRepository.findById(email)
                .orElseThrow(() -> new IllegalArgumentException("User Not Found"));
    }

}
