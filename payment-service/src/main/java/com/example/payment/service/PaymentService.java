package com.example.payment.service;

import com.example.payment.entity.PaymentTransaction;
import com.example.payment.exception.PaymentFaultException;
import com.example.payment.repository.PaymentTransactionRepository;
import java.math.BigDecimal;
import java.security.SecureRandom;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Implements payment business rules behind the SOAP endpoint. */
@Service
public class PaymentService {
  private final PaymentTransactionRepository repository;
  private final SecureRandom random = new SecureRandom();
  public PaymentService(PaymentTransactionRepository repository){this.repository=repository;}
  @Transactional public PaymentTransaction process(String orderId, BigDecimal amount, String method, String card) {
    if(card==null || !card.matches("\\d{16}")) throw new PaymentFaultException("cardNumber must be exactly 16 digits");
    if(amount==null || amount.signum()<=0) throw new PaymentFaultException("amount must be greater than zero");
    StringBuilder suffix=new StringBuilder(); for(int i=0;i<10;i++) suffix.append(Integer.toHexString(random.nextInt(16)).toUpperCase());
    String id="TXN-"+suffix;
    String status=card.endsWith("0000")?"DECLINED":"APPROVED";
    PaymentTransaction tx=new PaymentTransaction(id,orderId,amount,method,card.substring(12),status); repository.save(tx);
    return tx;
  }
  @Transactional(readOnly=true) public PaymentTransaction get(String id){return repository.findById(id).orElseThrow(()->new PaymentFaultException("Unknown transactionId '"+id+"'"));}
  @Transactional public PaymentTransaction refund(String id){PaymentTransaction tx=get(id); if(!"APPROVED".equals(tx.getStatus())) throw new PaymentFaultException("Only APPROVED payments can be refunded (current: "+tx.getStatus()+")"); tx.setStatus("REFUNDED"); return repository.save(tx);}
}
