package com.javaprojects.restaurant.controller;

import com.javaprojects.restaurant.infrastructure.entity.ReservationEntity;
import com.javaprojects.restaurant.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservation")
public class ReservationController {
    @Autowired
    private ReservationService reservationService;

    @PostMapping
    public ReservationEntity createReservation(@RequestBody ReservationEntity reservation) {
        return reservationService.createReservation(reservation);
    }

    @GetMapping
    public ReservationEntity getReservationByUserId(@RequestParam String userId) {
        return reservationService.getReservationByUserId(userId);
    }
}
