package com.javaprojects.restaurant.service;

import com.javaprojects.restaurant.infrastructure.entity.TableEntity;
import com.javaprojects.restaurant.infrastructure.repository.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public List<TableEntity> createTablesInBulk(String type, int quantity, int maxCapacity) {

        // Validação do tipo de mesa
        if (!type.equals("2") && !type.equals("5")) {
            throw new IllegalArgumentException("Tipo de mesa inválido. Escolha '2' ou '5'.");
        }

        // Configurações baseadas no tipo
        String prefix = type.equals("2") ? "B" : "A";
        int placesPerTable = Integer.parseInt(type);

        // Capacidade atual
        int currentCapacity = tableRepository.findAll().stream()
                .mapToInt(TableEntity::getPlaces)
                .sum();

        // Verifica a capacidade disponível
        int availableCapacity = maxCapacity - currentCapacity;
        int maxTablesAllowed = availableCapacity / placesPerTable;

        if (maxTablesAllowed <= 0) {
            throw new IllegalArgumentException("Capacidade máxima já atingida.");
        }

        // Ajusta a quantidade para não ultrapassar a capacidade
        quantity = Math.min(quantity, maxTablesAllowed);

        // Encontra o maior número já utilizado no prefixo
        int maxNumber = tableRepository.findAll().stream()
                .filter(t -> t.getName().startsWith(prefix))
                .mapToInt(t -> Integer.parseInt(t.getName().substring(1)))
                .max()
                .orElse(0);

        // Criação das mesas
        List<TableEntity> newTables = new ArrayList<>();
        for (int i = 1; i <= quantity; i++) {
            maxNumber++;
            TableEntity table = new TableEntity();
            table.setName(prefix + maxNumber);
            table.setPlaces(placesPerTable);
            table.setAvailable(true);
            newTables.add(table);
        }

        return tableRepository.saveAll(newTables);
    }
}
