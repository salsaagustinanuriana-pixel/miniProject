package com.example.salsa.service;

import com.example.salsa.model.WareHouse;
import com.example.salsa.model.WareHouseStock;
import com.example.salsa.repository.WarehouseRepository;
import com.example.salsa.repository.WarehouseStockRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class WarehouseStockService {

    private final WarehouseStockRepository warehouseStockRepository;
    private final WarehouseRepository warehouseRepository;

    public WarehouseStockService(WarehouseStockRepository warehouseStockRepository,
                                 WarehouseRepository warehouseRepository) {
        this.warehouseStockRepository = warehouseStockRepository;
        this.warehouseRepository = warehouseRepository;
    }


    public List<WareHouseStock> getAll() {
        return warehouseStockRepository.findAll();
    }


    public List<WareHouseStock> getByWarehouse(Long warehouseId) {

        WareHouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gudang tidak ditemukan"));

        return warehouseStockRepository.findAll().stream()
                .filter(stock -> stock.getWarehouse().getId().equals(warehouse.getId()))
                .toList();
    }


    public List<WareHouseStock> getByProduct(Long productId) {
        return warehouseStockRepository.findAll().stream()
                .filter(stock -> stock.getProduct().getId().equals(productId))
                .toList();
    }
}