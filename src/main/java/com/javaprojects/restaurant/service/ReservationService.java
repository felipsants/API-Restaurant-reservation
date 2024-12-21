package com.javaprojects.restaurant.service;

import com.javaprojects.restaurant.infrastructure.entity.TableEntity;
import com.javaprojects.restaurant.infrastructure.repository.ReservationRepository;
import com.javaprojects.restaurant.infrastructure.entity.ReservationEntity;
import com.javaprojects.restaurant.infrastructure.repository.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private TableService tableService;

    private static final int MAX_CAPACITY = 200;

    public ReservationEntity createReservation(ReservationEntity reservation) {
        int requiredSeats = reservation.getQuantity();

        // Verifica se a lotação máxima do dia foi atingida
        int totalReservationsForDay = reservationRepository.findByReservationDate(reservation.getReservationDate()).stream()
                .mapToInt(ReservationEntity::getQuantity)
                .sum();

        if (totalReservationsForDay + requiredSeats > MAX_CAPACITY) {
            throw new RuntimeException("Lotação máxima atingida para " + reservation.getReservationDate());
        }

        // Alocar mesas
        List<TableEntity> reservedTables = allocateTables(requiredSeats);

        // Associar mesas à reserva
        String tableNames = reservedTables.stream()
                .map(TableEntity::getName)
                .collect(Collectors.joining(", "));
        reservation.setTable(tableNames);

        // Salvar reserva
        return reservationRepository.save(reservation);
    }

    private List<TableEntity> allocateTables(int requiredSeats) {
        List<TableEntity> availableTables = tableRepository.findByAvailableTrueOrderByPlacesDesc();
        List<TableEntity> reservedTables = new ArrayList<>();
        int totalSeatsAllocated = 0;

        for (TableEntity table : availableTables) {
            if (totalSeatsAllocated < requiredSeats) {
                reservedTables.add(table);
                totalSeatsAllocated += table.getPlaces();

                if (totalSeatsAllocated >= requiredSeats) {
                    break;
                }
            }
        }

        if (totalSeatsAllocated < requiredSeats) {
            throw new RuntimeException("Não há mesas suficientes disponíveis para acomodar essa reserva.");
        }

        // Marcar mesas como indisponíveis
        reservedTables.forEach(table -> {
            table.setAvailable(false);
            tableRepository.save(table);
        });

        return reservedTables;
    }

    public ReservationEntity getReservationById(String id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reservation Not Found"));
    }

    public void cancelReservation(String reservationId, String reason) {
        ReservationEntity reservation = getReservationById(reservationId);

        if (reservation.isCanceled()) {
            throw new RuntimeException("Reservation Already Canceled");
        }

        reservation.setCanceled(true);
        reservation.setCancellationReason(reason);
        reservationRepository.save(reservation);

        // Liberar as mesas associadas à reserva
        String[] tableNames = reservation.getTable().split(", ");
        for (String tableName : tableNames) {
            TableEntity table = tableService.getTablesByName(tableName.trim());
            table.setAvailable(true);
            tableRepository.save(table);
        }
    }

    public void closeReservation(String reservationId){
        ReservationEntity reservation = getReservationById(reservationId);

        if (reservation.isClosed()){
            throw new RuntimeException("Reservation Already Closed");
        }

        reservation.setClosed(true);
        reservationRepository.save(reservation);

        String[] tableNames = reservation.getTable().split(", ");
        for (String tableName : tableNames) {
            TableEntity table = tableService.getTablesByName(tableName.trim());
            table.setAvailable(true);
            tableRepository.save(table);
        }
    }

    public List<ReservationEntity> getReservationsByDate(String date) {
        return reservationRepository.findByReservationDate(date);
    }

    public List<ReservationEntity> getReservationsByHour(String hour) {
        return reservationRepository.findByReservationHour(hour);
    }

    public List<ReservationEntity> getAllReservations() {
        return reservationRepository.findAll();
    }

    public ReservationEntity getReservationByUserEmail(String userEmail) {
        return reservationRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("Reservation Not Found"));
    }

    public void updateReservation(String userEmail, ReservationEntity updatedReservation) {
        ReservationEntity reservation = getReservationByUserEmail(userEmail);

        if (updatedReservation.getReservationDate() != null) {
            reservation.setReservationDate(updatedReservation.getReservationDate());
        }

        if (updatedReservation.getQuantity() > 0) {
            reservation.setQuantity(updatedReservation.getQuantity());
        }

        if (updatedReservation.isAnniversary()) {
            reservation.setAnniversary(true);
        }

        reservationRepository.save(reservation);
    }
}
