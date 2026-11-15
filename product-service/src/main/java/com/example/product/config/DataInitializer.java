package com.example.product.config;

import com.example.product.entity.Product;
import com.example.product.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
import java.math.BigDecimal;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner seedProducts(ProductRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                repository.saveAll(java.util.List.of(
                    new Product(null, "Mouse", "Wireless optical mouse", new BigDecimal("29.99"), 100),
                    new Product(null, "Keyboard", "Mechanical keyboard", new BigDecimal("79.99"), 50),
                    new Product(null, "Monitor", "27-inch 4K monitor", new BigDecimal("299.99"), 20),
                    new Product(null, "USB-C Hub", "Multi-port USB-C hub", new BigDecimal("39.99"), 75),
                    new Product(null, "Laptop Stand", "Adjustable laptop stand", new BigDecimal("49.99"), 0)
                ));
            }
        };
    }
}
