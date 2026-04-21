package com.example.salsa.controller;

import com.example.salsa.model.Product;
import com.example.salsa.model.WareHouseStock;
import com.example.salsa.request.ProductRequest;
import com.example.salsa.response.WebResponse;
import com.example.salsa.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    // Manual Constructor Injection
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public WebResponse<Product> create(@Valid @RequestBody ProductRequest request) {
       try {
           return WebResponse.<Product>builder()
                   .status("Success")
                   .message("Produk berhasil ditambahkan")
                   .data(productService.create(request))
                   .build();
       }catch (RuntimeException e) {
           return WebResponse.<Product>builder()
                   .status("failed")
                   .message(e.getMessage())
                   .data(null)
                   .build();
       }

    }

    @GetMapping
    public WebResponse<List<Product>> getAllProducts() {
        return WebResponse.<List<Product>>builder()
                .status("00")
                .message("Berhasil mengambil semua data product")
                .data(productService.getAllProduct())
                .build();
    }

    @PutMapping("/{id}")
    public WebResponse<Product> update(@PathVariable Long id, @Valid @RequestBody ProductRequest request) {
        return WebResponse.<Product>builder()
                .status("Success")
                .message("Produk berhasil diperbarui")
                .data(productService.update(id, request))
                .build();
    }

    @PutMapping("/delete/{id}")
    public WebResponse<Void> delete(@PathVariable Long id) {
        productService.softDelete(id);
        return WebResponse.<Void>builder()
                .status("Success")
                .message("Produk berhasil dinonaktifkan")
                .build();
    }

    @PutMapping("/restore/{id}")
    public WebResponse<Void> restore(@PathVariable Long id) {
        productService.restore(id);
        return WebResponse.<Void>builder()
                .status("Success")
             .message("Produk berhasil diaktifkan kembali")
                .build();
    }

    @GetMapping("/low-stock")
    public WebResponse<List<WareHouseStock>> getLowStock() {
        return WebResponse.<List<WareHouseStock>>builder()
                .status("Success")
                .message("List produk stok rendah")
                .data(productService.getLowStock())
                .build();
    }

    @GetMapping("/{sku}/stock-summary")
    public WebResponse<Map<String, Object>> getStockSummary(@PathVariable String sku) {

        Integer totalStock = productService.getStockSummary(sku);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("sku_code", sku);
        responseData.put("total_stok", totalStock);

        return WebResponse.<Map<String, Object>>builder()
                .status("Success")
                .message("Total stok seluruh gudang berhasil dihitung")
                .data(responseData)
                .build();
    }
}