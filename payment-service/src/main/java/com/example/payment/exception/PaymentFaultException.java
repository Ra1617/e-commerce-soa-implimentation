package com.example.payment.exception;

import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;
import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;

/** Client-visible SOAP fault for invalid payment requests or state. */
@SoapFault(faultCode = FaultCode.CLIENT)
public class PaymentFaultException extends RuntimeException {
  public PaymentFaultException(String message) { super(message); }
}
