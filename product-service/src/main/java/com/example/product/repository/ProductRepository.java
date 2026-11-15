package com.example.product.repository;

import com.example.product.entity.Product;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("update Product p set p.stock = p.stock - :quantity where p.id = :id and p.stock >= :quantity")
    int reserve(@Param("id") Long id, @Param("quantity") int quantity);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("update Product p set p.stock = p.stock + :quantity where p.id = :id")
    int release(@Param("id") Long id, @Param("quantity") int quantity);

    Optional<Product> findById(Long id);
}
