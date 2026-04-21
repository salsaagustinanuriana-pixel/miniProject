package com.example.salsa.repository;

import com.example.salsa.model.Product;
import com.example.salsa.model.WareHouseStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WarehouseStockRepository extends JpaRepository<WareHouseStock, Long> {

    Optional<WareHouseStock> findByProduct_IdAndWarehouse_Id(Long productId, Long warehouseId);

    List<WareHouseStock> findByProduct(Product product);
}