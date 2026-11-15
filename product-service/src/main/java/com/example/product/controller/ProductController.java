package com.example.product.controller;

import com.example.product.dto.*;
import com.example.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping({"/products", "/api/products"})
public class ProductController {
    private final ProductService service;
    public ProductController(ProductService service) { this.service = service; }

    @GetMapping public List<ProductResponse> all() {
        return service.findAll().stream().map(ProductResponse::from).toList();
    }
    @GetMapping("/{id}") public ProductResponse one(@PathVariable Long id) {
        return ProductResponse.from(service.findById(id));
    }
    @PostMapping("/{id}/reserve") public ProductResponse reserve(
            @PathVariable Long id, @Valid @RequestBody QuantityRequest request) {
        return ProductResponse.from(service.reserve(id, request.quantity()));
    }
    @RequestMapping(value = "/{id}/release", method = {RequestMethod.POST, RequestMethod.PUT})
    public ProductResponse release(
            @PathVariable Long id, @Valid @RequestBody QuantityRequest request) {
        return ProductResponse.from(service.release(id, request.quantity()));
    }
}
