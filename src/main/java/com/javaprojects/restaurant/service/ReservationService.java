package com.javaprojects.restaurant.service;

import com.javaprojects.restaurant.infrastructure.entity.TableEntity;
import com.javaprojects.restaurant.infrastructure.repository.ReservationRepository;
import com.javaprojects.restaurant.infrastructure.WaintingList.WaitingList;
import com.javaprojects.restaurant.infrastructure.entity.ReservationEntity;
import com.javaprojects.restaurant.infrastructure.repository.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    private static final int MAX_CAPACITY = 14;
    private WaitingList waitingList = new WaitingList();

    public ReservationEntity createReservation(ReservationEntity reservation) {
        int requiredSeats = reservation.getQuantity();
    
        // Verifica a lotação máxima do dia
        int totalReservationsForDay = reservationRepository.findByReservationDate(reservation.getReservationDate())
                .stream()
                .mapToInt(ReservationEntity::getQuantity)
                .sum();
    
        // Se a lotação máxima for atingida, adiciona na fila
        if (totalReservationsForDay + requiredSeats > MAX_CAPACITY) {
            
    
            // Retorna uma mensagem indicando que foi adicionado à fila
            throw new ResponseStatusException(HttpStatus.ACCEPTED, 
            "Lotação máxima atingida para " + reservation.getReservationDate());
        }
    
        // Alocar mesas disponíveis
        List<TableEntity> reservedTables = allocateTables(requiredSeats, reservation);
    
        // Associar mesas à reserva
        String tableNames = reservedTables.stream()
                .map(TableEntity::getName)
                .collect(Collectors.joining(", "));
        reservation.setTable(tableNames);
    
        // Salvar reserva
        return reservationRepository.save(reservation);
    }
    

    private List<TableEntity> allocateTables(int requiredSeats, ReservationEntity reservation) {
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
            // Adicionar o cliente na fila
            waitingList.addToQueue(reservation.getUserEmail());
            throw new ResponseStatusException(HttpStatus.ACCEPTED, 
            "Não há mesas suficientes disponíveis para acomodar essa reserva. Cliente adicionado à fila de espera.");
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

        if (!waitingList.isQueueEmpty()) {
            String nextInLine = waitingList.removeFromQueue();
            System.out.println("Chamando próximo cliente: " + nextInLine);
            // Aqui você pode enviar uma notificação ao cliente
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

    public String checkQueueStatus() {
        if (waitingList.isQueueEmpty()) {
            return "Fila vazia.";
        } else {
            return "Fila com " + waitingList.getQueueSize() + " pessoas. Próximo: " + waitingList.getFirstInQueue();
        }
    }
}
