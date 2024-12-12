package com.javaprojects.restaurant.service;

import com.javaprojects.restaurant.infrastructure.repository.ReservationRepository;
import com.javaprojects.restaurant.infrastructure.entity.ReservationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;

    public ReservationEntity createReservation(ReservationEntity reservation) {
        return reservationRepository.save(reservation);
    }

    public ReservationEntity getReservationById(String id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reservation Not Found"));
    }

    public ReservationEntity getReservationByUserId(String userId) {
        return reservationRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation Not Found"));
    }

    public List<ReservationEntity> getReservationsByDate(String date) {
        if (date != null) {
            // Lógica para buscar apenas por data
            return reservationRepository.findByReservationDate(date);
        }
        return reservationRepository.findAll(); // Se não passar data nem hora, retorna todas as reservas
    }

    public List<ReservationEntity> getReservationsByHour(String hour) {
        if (hour != null) {
            // Lógica para buscar apenas por data
            return reservationRepository.findByReservationHour(hour);
        }
        return reservationRepository.findAll();
    }

    public List<ReservationEntity> getAllReservations() {
        return reservationRepository.findAll();  // Busca todas as reservas
    }

}
