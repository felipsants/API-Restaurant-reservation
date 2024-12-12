package com.javaprojects.restaurant.controller;

import com.javaprojects.restaurant.infrastructure.entity.TableEntity;
import com.javaprojects.restaurant.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tables")
public class TableController {
    @Autowired
    private TableService tableService;

    @PostMapping
    public TableEntity addTable(@RequestBody TableEntity table) {
        return tableService.createTable(table);
    }

    @GetMapping
    public List<TableEntity> getAllTables() {
        return tableService.getAllTables();
    }

    @GetMapping("/{name}")
    public TableEntity getTableByName(@PathVariable String name) {
        return tableService.getTablesByName(name);
    }

    @GetMapping("/available/{available}")
    public List<TableEntity> getTablesByAvailable(@PathVariable boolean available) {
        return tableService.getTablesByAvailable(available);
    }
}
