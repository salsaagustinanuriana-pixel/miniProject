package com.example.salsa.repository;

import com.example.salsa.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByIdAndIsActiveTrue(Long id);

     Product findBySkuCode(String skuCode);
//    List<Product> findAllByIsActiveTrue();
    Optional<Product> findBySkuCodeAndIsActiveTrue(String skuCode);

}