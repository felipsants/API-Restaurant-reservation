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
        // Ordenar as mesas do maior para o menor número de lugares
        List<TableEntity> availableTables = tableRepository.findByAvailableTrueOrderByPlacesDesc();

        // Lista de mesas que serão reservadas
        List<TableEntity> reservedTables = new ArrayList<>();
        int totalSeatsAllocated = 0;

        // Primeiro, tenta alocar as maiores mesas possíveis
        for (TableEntity table : availableTables) {
            if (totalSeatsAllocated < requiredSeats) {
                reservedTables.add(table);
                totalSeatsAllocated += table.getPlaces();

                // Se já atingiu ou superou a quantidade necessária de lugares, pare
                if (totalSeatsAllocated >= requiredSeats) {
                    break;
                }
            }
        }

        // Verifica se foi possível alocar lugares suficientes
        if (totalSeatsAllocated < requiredSeats) {
            throw new RuntimeException("Não há mesas suficientes disponíveis para acomodar essa reserva.");
        }

        // Marcar as mesas como não disponíveis
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

    public ReservationEntity getReservationByUserEmail(String userEmail) {
        return reservationRepository.findByUserEmail(userEmail)
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

    public ReservationEntity updateReservation(String userEmail, ReservationEntity updatedReservation) {
        ReservationEntity reservation = getReservationByUserEmail(userEmail);

        if(updatedReservation.getReservationDate() != null) {
            reservation.setReservationDate(updatedReservation.getReservationDate());
        }

        if(updatedReservation.getReservationHour() != null) {
            reservation.setReservationHour(updatedReservation.getReservationHour());
        }

        return reservationRepository.save(reservation);
    }

}
