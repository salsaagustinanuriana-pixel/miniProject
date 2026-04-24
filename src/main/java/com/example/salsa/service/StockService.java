package com.example.salsa.service;

import com.example.salsa.model.*;
import com.example.salsa.repository.*;
import com.example.salsa.request.StockMutationRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StockService {

    private final WarehouseStockRepository warehouseStockRepository;
    private final StockMutationRepository stockMutationRepository;
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;

    public StockService(WarehouseStockRepository warehouseStockRepository,
                        StockMutationRepository stockMutationRepository,
                        ProductRepository productRepository,
                        WarehouseRepository warehouseRepository) {
        this.warehouseStockRepository = warehouseStockRepository;
        this.stockMutationRepository = stockMutationRepository;
        this.productRepository = productRepository;
        this.warehouseRepository = warehouseRepository;
    }

    @Transactional
    public void stockIn(StockMutationRequest request) {
        // 1. Cari Produk & Pastikan Aktif
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product tidak ditemukan"));

        if (!product.getIsActive()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Gagal! Product sudah tidak aktif");
        }

        // 2. Cari Warehouse
        WareHouse wareHouse = warehouseRepository.findById(request.getToWarehouseId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gudang tidak ditemukan"));

        // 3. Update/Insert WarehouseStock
        WareHouseStock stock = warehouseStockRepository.findByProduct_IdAndWarehouse_Id(product.getId(), wareHouse.getId())
                .orElse(new WareHouseStock());

        if (stock.getId() == null) {
            stock.setProduct(product);
            stock.setWarehouse(wareHouse);
            stock.setStock(request.getQuantity());
        } else {
            stock.setStock(stock.getStock() + request.getQuantity());
        }
        warehouseStockRepository.save(stock);


        StockMutation mutation = new StockMutation();
        mutation.setProduct(product);
        mutation.setToWarehouse(wareHouse);
        mutation.setQuantity(request.getQuantity());
        mutation.setType("IN");
        mutation.setTimestamp(LocalDateTime.now());

        stockMutationRepository.save(mutation);
    }

    @Transactional
    public void transfer(StockMutationRequest request) {

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product tidak ditemukan"));

        if (!product.getIsActive()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product tidak aktif");
        }

        WareHouse fromWarehouse = warehouseRepository.findById(request.getFromWarehouseId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gudang asal tidak ditemukan"));

        WareHouse toWarehouse = warehouseRepository.findById(request.getToWarehouseId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gudang tujuan tidak ditemukan"));

        WareHouseStock fromStock = warehouseStockRepository
                .findByProduct_IdAndWarehouse_Id(product.getId(), fromWarehouse.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Stok di gudang asal tidak ditemukan"));

        if (fromStock.getStock() < request.getQuantity()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Stok tidak mencukupi");
        }

        // KURANGI STOCK ASAL
        fromStock.setStock(fromStock.getStock() - request.getQuantity());
        warehouseStockRepository.save(fromStock);

        // TAMBAH STOCK TUJUAN
        WareHouseStock toStock = warehouseStockRepository
                .findByProduct_IdAndWarehouse_Id(product.getId(), toWarehouse.getId())
                .orElse(null);

        if (toStock == null) {
            toStock = new WareHouseStock();
            toStock.setProduct(product);
            toStock.setWarehouse(toWarehouse);
            toStock.setStock(request.getQuantity());
        } else {
            toStock.setStock(toStock.getStock() + request.getQuantity());
        }

        warehouseStockRepository.save(toStock);

        LocalDateTime now = LocalDateTime.now();

        // 🔥 OUT (dari gudang asal)
        StockMutation outMutation = new StockMutation();
        outMutation.setProduct(product);
        outMutation.setFromWarehouse(fromWarehouse);
        outMutation.setToWarehouse(toWarehouse);
        outMutation.setQuantity(request.getQuantity());
        outMutation.setType("OUT");
        outMutation.setTimestamp(now);

        stockMutationRepository.save(outMutation);

        // 🔥 IN (ke gudang tujuan)
        StockMutation inMutation = new StockMutation();
        inMutation.setProduct(product);
        inMutation.setFromWarehouse(fromWarehouse);
        inMutation.setToWarehouse(toWarehouse);
        inMutation.setQuantity(request.getQuantity());
        inMutation.setType("IN");
        inMutation.setTimestamp(now);

        stockMutationRepository.save(inMutation);
    }
    public List<StockMutation> getLatestMutation(String type) {

        if (type == null || type.isEmpty()) {
            type = "ALL";
        }

        type = type.toUpperCase();

        if (!type.equals("IN") && !type.equals("OUT") && !type.equals("ALL")) {
            throw new IllegalArgumentException("Type harus IN, OUT, atau ALL");
        }

        Pageable pageable = PageRequest.of(0, 5);

        return stockMutationRepository.findLatestMutation(type, pageable);
    }
}