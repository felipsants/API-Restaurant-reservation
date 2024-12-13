package com.javaprojects.restaurant.service;

import com.javaprojects.restaurant.infrastructure.entity.TableEntity;
import com.javaprojects.restaurant.infrastructure.repository.ReservationRepository;
import com.javaprojects.restaurant.infrastructure.entity.ReservationEntity;
import com.javaprojects.restaurant.infrastructure.repository.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private TableRepository tableRepository;

    public ReservationEntity createReservation(ReservationEntity reservation) {
        int requiredSeats = reservation.getQuantity();
        List<TableEntity> availableTables = tableRepository.findByAvailableTrueOrderByPlacesAsc();

        // Lista de mesas que serão reservadas
        List<TableEntity> reservedTables = new ArrayList<>();
        int totalSeatsAllocated = 0;

        for (TableEntity table : availableTables) {
            reservedTables.add(table);
            totalSeatsAllocated += table.getPlaces();

            // Parar se já atingimos o número necessário de lugares
            if (totalSeatsAllocated >= requiredSeats) {
                break;
            }
        }

        reservedTables.forEach(table -> {
            table.setAvailable(false);
            tableRepository.save(table);
        });

        // Associa as mesas reservadas à reserva
        String tableNames = reservedTables.stream()
                .map(TableEntity::getName)
                .collect(Collectors.joining(", "));
        reservation.setTable(tableNames);

        // Salvar e retornar a reserva
        return reservationRepository.save(reservation);
    }


    public ReservationEntity getReservationById(String id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reservation Not Found"));
    }

    public void deleteReservation(String reservationId) {
        ReservationEntity reservation = getReservationById(reservationId);

        // Identificar as mesas alocadas (usando o nome das mesas armazenado no campo `table`)
        String[] tableNames = reservation.getTable().split(", ");

        // Atualizar a disponibilidade das mesas
        for (String tableName : tableNames) {
            TableEntity table = tableRepository.findByName(tableName)
                    .orElseThrow(() -> new RuntimeException("Table not found: " + tableName));
            table.setAvailable(true);
            tableRepository.save(table);
        }

        // Remover a reserva
        reservationRepository.deleteById(reservationId);
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
