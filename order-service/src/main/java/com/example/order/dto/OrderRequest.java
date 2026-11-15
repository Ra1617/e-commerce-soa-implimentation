package com.example.order.dto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;
/** Validated request for the order workflow. */
public record OrderRequest(@NotBlank String customerName,@NotEmpty @Valid List<Item> items,@NotNull @Valid Payment payment) {
 public record Item(@NotNull Long productId,@Min(1) @Max(100) int quantity){}
 public record Payment(@NotBlank @Pattern(regexp="CARD|DEBIT_CARD") String method,@NotBlank @Pattern(regexp="\\d{16}", message="must be exactly 16 digits") String cardNumber){}
}
