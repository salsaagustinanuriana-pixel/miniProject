package com.example.salsa.service;

import com.example.salsa.model.WareHouse;
import com.example.salsa.repository.WarehouseRepository;
import com.example.salsa.request.WarehouseRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;

    public WarehouseService(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    @Transactional
    public WareHouse createWarehouse(WarehouseRequest request) {
        if (warehouseRepository.findByCode(request.getCode()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Kode gudang sudah ada!");
        }

        WareHouse wareHouse = new WareHouse();
        wareHouse.setCode(request.getCode());
        wareHouse.setName(request.getName());

        return warehouseRepository.save(wareHouse);
    }
}