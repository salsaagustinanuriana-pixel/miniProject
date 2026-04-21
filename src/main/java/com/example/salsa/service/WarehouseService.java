package com.example.salsa.service;

import com.example.salsa.exception.NotFoundException;
import com.example.salsa.model.WareHouse;
import com.example.salsa.repository.WarehouseRepository;
import com.example.salsa.request.WarehouseRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

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

    public WareHouse update(Long id, WarehouseRequest request) {
        if (warehouseRepository.existsByCodeAndIdNot(request.getCode(), id)) {
            throw new IllegalArgumentException("Kode gudang sudah digunakan: " + request.getCode());
        }


        WareHouse wareHouse = warehouseRepository.findById(id).orElseThrow(()-> new NotFoundException("warehouse tidak di temukan" + id));
        wareHouse.setName(request.getName());
        wareHouse.setCode(request.getCode());

        return warehouseRepository.save(wareHouse);
    }

    public List<WareHouse> getAll() {
        return warehouseRepository.findAll();
    }
}