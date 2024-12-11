package com.javaprojects.restaurant.infrastructure.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "ReservationEntity")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationEntity {

    @Id
    private String Id;
    private String UserId;
    private String table;
    private LocalDateTime reservationDate;
    private int quantity;
}
