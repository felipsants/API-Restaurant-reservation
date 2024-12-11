package com.javaprojects.restaurant.infrastructure.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "reservation_Entity")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationEntity {

    @Id
    private String id;
    private String userId;
    private String table;
    private LocalDateTime reservationDate;
    private int quantity;
}
