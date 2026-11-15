package com.example.product.service;

import com.example.product.entity.Product;
import com.example.product.exception.*;
import com.example.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@Service
public class ProductService {
    private static final Logger log = LoggerFactory.getLogger(ProductService.class);
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
        Product product = findById(id);
        log.info("[PRODUCT] Reserved {} of product {}, stock now {}", quantity, id, product.getStock());
        return product;
    }
    @Transactional
    public Product release(Long id, int quantity) {
        ensureExists(id);
        repository.release(id, quantity);
        Product product = findById(id);
        log.info("[PRODUCT] Released {} of product {}, stock now {}", quantity, id, product.getStock());
        return product;
    }
    private void ensureExists(Long id) { if (!repository.existsById(id)) throw new ProductNotFoundException(id); }
}
