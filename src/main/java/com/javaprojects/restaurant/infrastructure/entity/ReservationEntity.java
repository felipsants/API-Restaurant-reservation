package com.javaprojects.restaurant.infrastructure.entity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "reservation_entity")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationEntity {
    @Id
    public String id;
    public String userEmail;
    public String table;
    public String reservationDate;
    public String reservationHour;
    public boolean anniversary;
    public int quantity;
    
    @Builder.Default
    public boolean canceled = false;
    @Builder.Default
    public boolean closed = false;
    public String cancellationReason;
}
