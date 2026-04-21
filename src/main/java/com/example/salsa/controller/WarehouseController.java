package com.example.salsa.controller;

import com.example.salsa.model.WareHouse;
import com.example.salsa.request.WarehouseRequest;
import com.example.salsa.response.WebResponse;
import com.example.salsa.service.WarehouseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PutMapping("/{id}")
    public ResponseEntity<WebResponse<WareHouse>> update(@PathVariable Long id, @Valid @RequestBody WarehouseRequest request) {
        WareHouse wareHouse = warehouseService.update(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(
        WebResponse.<WareHouse>builder()
                .status("Success")
                .message("Gudang berhasil diupdate")
                .data(wareHouse)
                .build()

      );
    }
    @GetMapping
    public ResponseEntity<WebResponse<List<WareHouse>>> getAll() {

        List<WareHouse> warehouses = warehouseService.getAll();

        WebResponse<List<WareHouse>> response = WebResponse.<List<WareHouse>>builder()
                .status("Success")
                .message("Berhasil mengambil semua data gudang")
                .data(warehouses)
                .build();

        return ResponseEntity.ok(response);
    }
}