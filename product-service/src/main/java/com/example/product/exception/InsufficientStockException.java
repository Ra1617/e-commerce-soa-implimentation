package com.example.product.exception;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(Long id) { super("Insufficient stock for product " + id); }
}
