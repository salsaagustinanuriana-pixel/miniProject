package com.example.salsa.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WareHouseStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private WareHouse warehouse;

    @Column(name = "stock")
    private Integer stock = 0;

    public Integer getStock() {
        if (stock == null){
            return 0;
        }
        return stock;
    }

    @Version
    private Long version;


}