package com.example.order.entity;
import jakarta.persistence.*; import java.math.BigDecimal;
/** Purchased product snapshot stored with the aggregate. */
@Entity public class OrderItem { @Id @GeneratedValue private Long id; private Long productId; private String name; private BigDecimal unitPrice; private int quantity; private BigDecimal lineTotal; protected OrderItem(){} public OrderItem(Long p,String n,BigDecimal u,int q,BigDecimal l){productId=p;name=n;unitPrice=u;quantity=q;lineTotal=l;} public Long getProductId(){return productId;} public int getQuantity(){return quantity;} }
