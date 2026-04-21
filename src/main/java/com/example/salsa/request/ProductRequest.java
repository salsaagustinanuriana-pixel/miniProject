package com.example.salsa.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProductRequest {
    @NotBlank(message = "SKU tidak boleh kosong")
    private String skuCode;

    @NotBlank(message = "Nama tidak boleh kosong")
    private String name;

    @Min(value = 1000, message = "Harga minimal 1000")
    private Double price;

    private Long categoryId;
}