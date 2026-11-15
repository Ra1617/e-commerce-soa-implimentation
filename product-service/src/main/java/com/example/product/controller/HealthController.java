package com.example.product.controller;

import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
public class HealthController {
    @GetMapping({"/health", "/api/health"})
    public Map<String, String> health() {
        return Map.of("service", "product", "status", "UP");
    }
}
