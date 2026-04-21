package com.example.salsa.controller;

import com.example.salsa.model.Category;
import com.example.salsa.response.WebResponse;
import com.example.salsa.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public WebResponse<Category> create(@RequestBody Category request) {
        return WebResponse.<Category>builder()
                .status("Success")
                .message("Category berhasil dibuat")
                .data(categoryService.create(request))
                .build();
    }

    @GetMapping
    public WebResponse<List<Category>> getAll() {
        return WebResponse.<List<Category>>builder()
                .status("Success")
                .message("List category")
                .data(categoryService.getAll())
                .build();
    }
}