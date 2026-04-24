package com.example.salsa.controller;

import com.example.salsa.model.StockMutation;
import com.example.salsa.request.StockMutationRequest;
import com.example.salsa.response.WebResponse;
import com.example.salsa.service.StockService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping("/in")
    public WebResponse<Void> stockIn(@Valid @RequestBody StockMutationRequest request) {
        stockService.stockIn(request);
        return WebResponse.<Void>builder()
                .status("Success")
                .message("Stok berhasil ditambahkan")
                .build();
    }

    @PostMapping("/transfer")
    public WebResponse<Void> transfer(@Valid @RequestBody StockMutationRequest request) {
        stockService.transfer(request);
        return WebResponse.<Void>builder()
                .status("Success")
                .message("Transfer stok berhasil dilakukan")
                .build();
    }

    @GetMapping("/stock-mutations")
    public ResponseEntity<List<StockMutation>> getLatestMutations(
            @RequestParam(defaultValue = "ALL") String type
    ) {
        return ResponseEntity.ok(
                stockService.getLatestMutation(type)
        );
    }
    }
