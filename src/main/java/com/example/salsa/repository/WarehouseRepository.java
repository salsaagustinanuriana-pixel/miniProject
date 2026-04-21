package com.example.salsa.repository;

import com.example.salsa.model.WareHouse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WarehouseRepository extends JpaRepository<WareHouse, Long> {

    Optional<WareHouse> findByCode(String code);

    boolean existsByCodeAndIdNot(String code , Long id);

}