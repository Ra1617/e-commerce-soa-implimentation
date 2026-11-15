package com.example.payment.endpoint;

import com.example.payment.service.PaymentService;
import java.math.BigDecimal;
import org.springframework.ws.server.endpoint.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.xml.transform.*;
import jakarta.xml.bind.JAXBElement;
import com.example.payment.*;

/** Translates the generated XML contract into payment service calls. */
@Component
@Endpoint
public class PaymentEndpoint {
  private static final String NS="http://ecommerce.example.com/payment";
  private final PaymentService service;
  public PaymentEndpoint(PaymentService service){this.service=service;}
  @PayloadRoot(namespace=NS,localPart="ProcessPaymentRequest") @ResponsePayload
  public ProcessPaymentResponse process(@RequestPayload ProcessPaymentRequest r){var t=service.process(r.getOrderId(),r.getAmount(),r.getMethod(),r.getCardNumber());var out=new ProcessPaymentResponse();out.setTransactionId(t.getTransactionId());out.setStatus(t.getStatus());out.setMessage("DECLINED".equals(t.getStatus())?"Card declined by issuer":"Payment successful");return out;}
  @PayloadRoot(namespace=NS,localPart="GetPaymentStatusRequest") @ResponsePayload
  public GetPaymentStatusResponse status(@RequestPayload GetPaymentStatusRequest r){var t=service.get(r.getTransactionId());var out=new GetPaymentStatusResponse();out.setTransactionId(t.getTransactionId());out.setStatus(t.getStatus());return out;}
  @PayloadRoot(namespace=NS,localPart="RefundPaymentRequest") @ResponsePayload
  public RefundPaymentResponse refund(@RequestPayload RefundPaymentRequest r){var t=service.refund(r.getTransactionId());var out=new RefundPaymentResponse();out.setTransactionId(t.getTransactionId());out.setStatus(t.getStatus());out.setMessage("Payment refunded");return out;}
}
