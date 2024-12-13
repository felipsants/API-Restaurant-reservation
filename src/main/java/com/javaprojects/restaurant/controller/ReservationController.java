package com.javaprojects.restaurant.controller;

import com.javaprojects.restaurant.infrastructure.entity.ReservationEntity;
import com.javaprojects.restaurant.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservation")
public class ReservationController {
    @Autowired
    private ReservationService reservationService;

    @PostMapping
    public ReservationEntity createReservation(@RequestBody ReservationEntity reservation) {
        return reservationService.createReservation(reservation);
    }

    @GetMapping("/{id}")
    public ReservationEntity getReservationsById(@PathVariable String id) {
        return reservationService.getReservationById(id);
    }

    @GetMapping("/user/{userId}")
    public ReservationEntity getReservationsByUserId(@PathVariable String userId) {
        return reservationService.getReservationByUserId(userId);
    }

    @GetMapping("/date/{reservationDate}")
    public List<ReservationEntity> getReservationsDate(@PathVariable String reservationDate) {
        return reservationService.getReservationsByDate(reservationDate);
    }

    @GetMapping("/hour/{reservationHour}")
    public List<ReservationEntity> getReservationsHour(@PathVariable String reservationHour) {
        return reservationService.getReservationsByHour(reservationHour);
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<String> deleteReservation(@PathVariable String id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.ok("Reservation deleted and tables released.");
    }

    @GetMapping
    public List<ReservationEntity> getAllReservations() {
        return reservationService.getAllReservations();
    }
}
