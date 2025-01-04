package com.javaprojects.restaurant.controller;

import com.javaprojects.restaurant.infrastructure.entity.ReservationCancelReason;
import com.javaprojects.restaurant.infrastructure.entity.ReservationEntity;
import com.javaprojects.restaurant.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservation")
public class ReservationController {
    @Autowired
    private ReservationService reservationService;

    @PostMapping
    public ResponseEntity<?> createReservation(@RequestBody ReservationEntity reservation) {
        try {
            ReservationEntity createdReservation = reservationService.createReservation(reservation);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdReservation);
        } catch (RuntimeException e) {
            if (e.getMessage().startsWith("Lotação máxima atingida")) {
                // Cliente foi adicionado à fila, retornar status 202 (Accepted)
                return ResponseEntity.status(HttpStatus.ACCEPTED).body("Adicionado à fila de espera.");
            }
            // Erros gerais retornam 400
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao criar reserva: " + e.getMessage());
        }
    }
    

    @GetMapping("/{id}")
    public ReservationEntity getReservationsById(@PathVariable String id) {
        return reservationService.getReservationById(id);
    }

    @GetMapping("/user/{userEmail}")
    public ReservationEntity getReservationsByUserEmail(@PathVariable String userEmail) {
        return reservationService.getReservationByUserEmail(userEmail);
    }

    @GetMapping("/date/{reservationDate}")
    public List<ReservationEntity> getReservationsDate(@PathVariable String reservationDate) {
        return reservationService.getReservationsByDate(reservationDate);
    }

    @GetMapping("/hour/{reservationHour}")
    public List<ReservationEntity> getReservationsHour(@PathVariable String reservationHour) {
        return reservationService.getReservationsByHour(reservationHour);
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<String> cancelReservation(@PathVariable String id, @RequestBody ReservationCancelReason request) {
        try{
            reservationService.cancelReservation(id, request.getReason());
            return ResponseEntity.ok("Reservation Cancelled");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/close/{id}")
    public ResponseEntity<String> closeReservation(@PathVariable String id) {
        try{
            reservationService.closeReservation(id);
            return ResponseEntity.ok("Reservation Closed");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping
    public List<ReservationEntity> getAllReservations() {
        return reservationService.getAllReservations();
    }

    @PutMapping("/updated/{userEmail}")
    public ResponseEntity<ReservationEntity> updateReservation(@PathVariable String userEmail, @RequestBody ReservationEntity reservation) {
        try{
            reservationService.updateReservation(userEmail, reservation);
            return ResponseEntity.ok(reservation);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/queue/status")
    public ResponseEntity<String> checkQueueStatus() {
        return ResponseEntity.ok(reservationService.checkQueueStatus());
    }
}
