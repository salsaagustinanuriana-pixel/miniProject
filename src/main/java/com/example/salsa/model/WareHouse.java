package com.example.salsa.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data

public class WareHouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Kode gudang tidak boleh kosong!")
    @Column(unique = true)
    private String code;

    @NotBlank(message = "Nama gudang tidak boleh kosong!")
    private String name;
}
