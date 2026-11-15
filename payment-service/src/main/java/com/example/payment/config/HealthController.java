package com.example.payment.config;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
/** Basic liveness endpoint used by demonstrations. */
@RestController public class HealthController { @GetMapping("/health") public Map<String,String> health(){return Map.of("service","payment","status","UP");} }
