package com.example.payment.repository;
import com.example.payment.entity.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
/** Persistence boundary for payment transactions. */
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction,String> {}
