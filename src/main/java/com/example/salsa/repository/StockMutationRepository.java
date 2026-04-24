package com.example.salsa.repository;

import com.example.salsa.model.StockMutation;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface StockMutationRepository extends JpaRepository<StockMutation, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE StockMutation s SET s.product = null WHERE s.product.id = :productId")
    void setProductNull(@Param("productId") Long productId);

}

























//    @Modifying
//    @Query("UPDATE StockMutation sm SET sm.product = null WHERE sm.product.id = :productId")
//    static void setProductNull(@Param("productId") Long productId);