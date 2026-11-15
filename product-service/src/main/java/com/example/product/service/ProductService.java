package com.example.product.service;

import com.example.product.entity.Product;
import com.example.product.exception.*;
import com.example.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository repository;
    public ProductService(ProductRepository repository) { this.repository = repository; }

    @Transactional(readOnly = true)
    public List<Product> findAll() { return repository.findAll(); }
    @Transactional(readOnly = true)
    public Product findById(Long id) { return repository.findById(id).orElseThrow(() -> new ProductNotFoundException(id)); }

    @Transactional
    public Product reserve(Long id, int quantity) {
        ensureExists(id);
        if (repository.reserve(id, quantity) == 0) throw new InsufficientStockException(id);
        return findById(id);
    }
    @Transactional
    public Product release(Long id, int quantity) {
        ensureExists(id);
        repository.release(id, quantity);
        return findById(id);
    }
    private void ensureExists(Long id) { if (!repository.existsById(id)) throw new ProductNotFoundException(id); }
}
