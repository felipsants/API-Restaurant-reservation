package com.javaprojects.restaurant.infrastructure.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "reservation_reason")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationCancelReason {
    private String reason;
}
