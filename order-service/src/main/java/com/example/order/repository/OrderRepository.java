package com.example.order.repository;
import com.example.order.entity.Order; import org.springframework.data.jpa.repository.JpaRepository;
/** Order persistence boundary. */
public interface OrderRepository extends JpaRepository<Order,String> {}
