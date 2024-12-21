package com.javaprojects.restaurant.controller;

import com.javaprojects.restaurant.infrastructure.entity.TableEntity;
import com.javaprojects.restaurant.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/bulk-create")
    public ResponseEntity<?> createTablesInBulk(
            @RequestParam("type") String type,
            @RequestParam("quantity") int quantity,
            @RequestParam("maxCapacity") int maxCapacity) {

        try {
            List<TableEntity> tables = tableService.createTablesInBulk(type, quantity, maxCapacity);
            return ResponseEntity.ok(tables);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
