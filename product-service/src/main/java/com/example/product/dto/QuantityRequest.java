package com.example.product.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record QuantityRequest(
        @NotNull @Min(1) @JsonAlias({"amount", "count"}) Integer quantity) {}
