package com.javaprojects.restaurant.service;

import com.javaprojects.restaurant.infrastructure.entity.TableEntity;
import com.javaprojects.restaurant.infrastructure.repository.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TableService {
    @Autowired
    private TableRepository tableRepository;

    public TableEntity createTable(TableEntity table) {
        return tableRepository.save(table);
    }

    public List<TableEntity> getAllTables() {
        return tableRepository.findAll();
    }

    public TableEntity getTableById(String id) {
        return tableRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Table not found"));
    }

    public TableEntity getTablesByName(String name) {
        return tableRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Table not found"));
    }

    public List<TableEntity> getTablesByAvailable(Boolean available) {
        return tableRepository.findByAvailable(available);
    }

}
