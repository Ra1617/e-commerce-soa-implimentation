package com.example.order.entity;
import jakarta.persistence.*; import java.math.BigDecimal; import java.time.Instant; import java.util.*;
/** Aggregate root for an order and its immutable purchased lines. */
@Entity @Table(name="customer_orders")
public class Order {
 @Id private String orderId; private String customerName; private BigDecimal totalAmount; private String status; private String transactionId; private String paymentCardLast4; private String failureReason; private Instant createdAt;
 @OneToMany(cascade=CascadeType.ALL,orphanRemoval=true) private List<OrderItem> items=new ArrayList<>();
 protected Order(){} public Order(String id,String customer,BigDecimal total,String status,String last4){orderId=id;customerName=customer;totalAmount=total;this.status=status;paymentCardLast4=last4;createdAt=Instant.now();}
 public String getOrderId(){return orderId;} public String getStatus(){return status;} public void setStatus(String s){status=s;} public String getTransactionId(){return transactionId;} public void setTransactionId(String s){transactionId=s;} public void setFailureReason(String s){failureReason=s;} public List<OrderItem> getItems(){return items;} public BigDecimal getTotalAmount(){return totalAmount;}
}
