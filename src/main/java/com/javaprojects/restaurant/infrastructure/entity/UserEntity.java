package com.javaprojects.restaurant.infrastructure.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "UserEntity")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UserEntity {
    @Id
    public String id;
    public String name;
    public Long PhoneNumber;
    public String email;
    public boolean anniversary;
}
