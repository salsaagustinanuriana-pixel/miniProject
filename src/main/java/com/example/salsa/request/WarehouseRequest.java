package com.example.salsa.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class WarehouseRequest {

    @NotBlank(message = "Kode warehouse tidak boleh kosong")
    private String code;

    @NotBlank(message = "Nama warehouse tidak boleh kosong")
    private String name;
}