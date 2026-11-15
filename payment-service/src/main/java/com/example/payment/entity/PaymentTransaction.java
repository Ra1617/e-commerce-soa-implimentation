package com.example.payment.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

/** Stores payment facts while deliberately retaining only card last-four digits. */
@Entity
public class PaymentTransaction {
  @Id private String transactionId;
  private String orderId;
  private BigDecimal amount;
  private String method;
  private String last4;
  private String status;
  private Instant createdAt;
  protected PaymentTransaction() {}
  public PaymentTransaction(String id,String order,BigDecimal amount,String method,String last4,String status){this.transactionId=id;this.orderId=order;this.amount=amount;this.method=method;this.last4=last4;this.status=status;this.createdAt=Instant.now();}
  public String getTransactionId(){return transactionId;} public String getStatus(){return status;} public void setStatus(String s){status=s;}
}
