package com.javaprojects.restaurant.infrastructure.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "tables")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class TableEntity {

    @Id
    public String id;
    public String name;
    public int places;
    public boolean available;
}
