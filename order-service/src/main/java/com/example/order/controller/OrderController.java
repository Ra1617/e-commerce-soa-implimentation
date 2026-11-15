package com.example.order.controller;
import com.example.order.dto.OrderRequest; import com.example.order.entity.Order; import com.example.order.service.OrderService; import jakarta.validation.Valid; import java.util.*; import org.springframework.http.*; import org.springframework.web.bind.annotation.*;
/** Thin HTTP adapter for order orchestration use cases. */
@RestController @RequestMapping("/orders") public class OrderController {
 private final OrderService service; public OrderController(OrderService s){service=s;}
 @PostMapping public ResponseEntity<Order> place(@Valid @RequestBody OrderRequest r){Order o=service.place(r);return ResponseEntity.status("PAYMENT_FAILED".equals(o.getStatus())?402:201).body(o);}
 @GetMapping public List<Order> all(){return service.all();} @GetMapping("/{id}") public Order one(@PathVariable String id){return service.one(id);} @PostMapping("/{id}/cancel") public Order cancel(@PathVariable String id){return service.cancel(id);}
}
