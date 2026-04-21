package com.example.salsa.controller;

import com.example.salsa.model.WareHouseStock;
import com.example.salsa.response.WebResponse;
import com.example.salsa.service.WarehouseStockService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/warehousestock")
public class WarehouseStockController {

    private final WarehouseStockService warehouseStockService;

    public WarehouseStockController(WarehouseStockService warehouseStockService) {
        this.warehouseStockService = warehouseStockService;
    }
    
    @GetMapping
    public WebResponse<List<WareHouseStock>> getAll() {
        return WebResponse.<List<WareHouseStock>>builder()
                .status("Success")
                .message("Semua data stok")
                .data(warehouseStockService.getAll())
                .build();
    }


    @GetMapping("/warehouse/{id}")
    public WebResponse<List<WareHouseStock>> getByWarehouse(@PathVariable Long id) {
        return WebResponse.<List<WareHouseStock>>builder()
                .status("Success")
                .message("Stok berdasarkan gudang")
                .data(warehouseStockService.getByWarehouse(id))
                .build();
    }


    @GetMapping("/product/{id}")
    public WebResponse<List<WareHouseStock>> getByProduct(@PathVariable Long id) {
        return WebResponse.<List<WareHouseStock>>builder()
                .status("Success")
                .message("Stok berdasarkan produk")
                .data(warehouseStockService.getByProduct(id))
                .build();
    }
}