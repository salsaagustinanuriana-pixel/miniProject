package com.example.salsa.controller;

import com.example.salsa.model.WareHouse;
import com.example.salsa.request.WarehouseRequest;
import com.example.salsa.response.WebResponse;
import com.example.salsa.service.WarehouseService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/warehouses")
public class WarehouseController {

    private final WarehouseService warehouseService;

    // Manual Constructor Injection
    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @PostMapping
    public WebResponse<WareHouse> create(@Valid @RequestBody WarehouseRequest request) {
        WareHouse warehouse = warehouseService.createWarehouse(request);

        return WebResponse.<WareHouse>builder()
                .status("Success")
                .message("Gudang baru berhasil ditambahkan")
                .data(warehouse)
                .build();
    }
}