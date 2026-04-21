package com.example.salsa.service;

import com.example.salsa.model.Category;
import com.example.salsa.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category create(Category request) {
        return categoryRepository.save(request);
    }

    public List<Category> getAll() {
        return categoryRepository.findAll();
    }
}