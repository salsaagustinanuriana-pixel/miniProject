package com.example.salsa.service;

import com.example.salsa.exception.NotFoundException;
import com.example.salsa.model.Category;
import com.example.salsa.model.Product;
import com.example.salsa.model.WareHouseStock;
import com.example.salsa.repository.CategoryRepository;
import com.example.salsa.repository.ProductRepository;
import com.example.salsa.repository.WarehouseStockRepository;
import com.example.salsa.request.ProductRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final WarehouseStockRepository warehouseStockRepository;


    public Product create(ProductRequest request) {

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category tidak ditemukan"));

        if (request.getSkuCode() != null ) {
            Product product = productRepository.findBySkuCode(request.getSkuCode());
            if (product != null) {
                throw new RuntimeException("produk dengan sku " + request.getSkuCode()+ "sudah ada");
            }
        }

        Product product = new Product();
        product.setSkuCode(request.getSkuCode());
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setCategory(category);
        product.setIsActive(true);

        return productRepository.save(product);
    }



    public Product update(Long id, ProductRequest request) {

        Product product = productRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new NotFoundException("Product tidak ditemukan atau nonaktif"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category tidak ditemukan"));

        product.setSkuCode(request.getSkuCode());
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setCategory(category);

        return productRepository.save(product);
    }


    public void softDelete(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product tidak ditemukan"));

        product.setIsActive(false);

        productRepository.save(product);
    }


    public void restore(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product tidak ditemukan"));

        product.setIsActive(true);

        productRepository.save(product);
    }

    public List<WareHouseStock> getLowStock() {
        return warehouseStockRepository.findAll().stream()
                .filter(ws -> ws.getProduct() != null && ws.getProduct().getIsActive())
                .filter(ws -> ws.getStock() <= 10)
                .toList();
    }

    public Integer getStockSummary(String skuCode) {

        Product product = productRepository.findBySkuCodeAndIsActiveTrue(skuCode)
                .orElseThrow(() -> new NotFoundException("Product tidak ditemukan atau nonaktif"));

        List<WareHouseStock> stocks = warehouseStockRepository.findByProduct(product);

        int totalStock = 0;

        for (WareHouseStock stock : stocks) {
            totalStock += stock.getStock();
        }

        return totalStock;
    }


    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

}