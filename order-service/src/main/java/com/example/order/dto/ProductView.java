package com.example.order.dto;
import java.math.BigDecimal;
/** Minimal catalogue representation consumed by the orchestrator. */
public record ProductView(Long id,String name,String category,BigDecimal price,int stock) {}
