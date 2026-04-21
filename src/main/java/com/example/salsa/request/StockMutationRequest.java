package com.example.salsa.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockMutationRequest {

    @NotNull(message = "Product ID tidak boleh kosong")
    private Long productId;

    // Untuk Stock In, ini adalah ID gudang tujuan
    @NotNull(message = "Gudang tujuan tidak boleh kosong")
    private Long toWarehouseId;

    // Field ini opsional, dipakai nanti pas Fitur C (Transfer)
    private Long fromWarehouseId;

    @NotNull(message = "Quantity tidak boleh kosong")
    @Min(value = 1, message = "Quantity minimal 1")
    private Integer quantity;

    // Field ini untuk menentukan "IN" atau "TRANSFER"
    private String type;
}