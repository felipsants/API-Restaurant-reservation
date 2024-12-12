package com.javaprojects.restaurant.infrastructure.entity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "reservation_entity")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationEntity {
    @Id
    public String id;
    public String userId;
    public String table;
    public String reservationDate;
    public String reservationHour;
    public int quantity;
}
