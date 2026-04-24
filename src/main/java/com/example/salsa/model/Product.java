package com.example.salsa.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_active")
    private Boolean isActive;

    @NotBlank(message = "sku code tidak boleh kosong")
    @Column(name = "sku_code", unique = true, nullable = false)
    private String skuCode;

    @NotBlank(message = "nama produk tidak boleh kosong!")
    private String name;

    @Min(value = 0,message = "Harga tidak boleh negatif")
    private double price;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Version
    private Long version;
}