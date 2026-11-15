package com.example.order.client;
import com.example.order.soap.*; import java.math.BigDecimal; import org.springframework.stereotype.Component; import org.springframework.ws.client.core.WebServiceTemplate;
/** Consumer-side SOAP stub generated from the provider's copied contract. */
@Component public class PaymentClient {
 private final WebServiceTemplate template; private final String uri; public PaymentClient(WebServiceTemplate t,org.springframework.core.env.Environment e){template=t;uri=e.getProperty("payment.uri","http://localhost:8003/ws");}
 public ProcessPaymentResponse process(String id,BigDecimal amount,String method,String card){var r=new ProcessPaymentRequest();r.setOrderId(id);r.setAmount(amount);r.setMethod(method);r.setCardNumber(card);return (ProcessPaymentResponse)template.marshalSendAndReceive(uri,r);}
 public void refund(String id){var r=new RefundPaymentRequest();r.setTransactionId(id);template.marshalSendAndReceive(uri,r);}
}
