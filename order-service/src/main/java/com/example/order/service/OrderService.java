package com.example.order.service;
import com.example.order.client.*; import com.example.order.dto.*; import com.example.order.entity.*; import com.example.order.exception.ApiException; import com.example.order.repository.OrderRepository; import com.example.order.soap.ProcessPaymentResponse; import java.math.BigDecimal; import java.util.*; import java.util.concurrent.ThreadLocalRandom; import org.slf4j.*; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional; import org.springframework.web.client.*; import org.springframework.ws.client.WebServiceIOException;
/** Coordinates reserve-pay-confirm and compensating release/refund workflows. */
@Service public class OrderService {
 private static final Logger log=LoggerFactory.getLogger(OrderService.class); private final ProductClient products; private final PaymentClient payments; private final OrderRepository orders;
 public OrderService(ProductClient p,PaymentClient pay,OrderRepository o){products=p;payments=pay;orders=o;}
 @Transactional public Order place(OrderRequest request){
  String id="ORD-"+random(8); List<OrderRequest.Item> reserved=new ArrayList<>(); List<OrderItem> lines=new ArrayList<>(); BigDecimal total=BigDecimal.ZERO;
  // 1. Read product snapshots and calculate the total.
  for(var item:request.items()){ProductView p;try{p=products.get(item.productId());}catch(HttpClientErrorException.NotFound e){throw new ApiException(404,"Product "+item.productId()+" not found");}catch(RestClientException e){throw new ApiException(503,"Product Service unavailable");} if(p==null)throw new ApiException(404,"Product "+item.productId()+" not found"); BigDecimal line=p.price().multiply(BigDecimal.valueOf(item.quantity())); lines.add(new OrderItem(p.id(),p.name(),p.price(),item.quantity(),line));total=total.add(line);}
  // 2. Reserve each line; compensate earlier reservations if one fails.
  for(var item:request.items()){try{products.reserve(item.productId(),item.quantity());reserved.add(item);}catch(HttpClientErrorException.Conflict e){release(reserved);throw new ApiException(409,e.getResponseBodyAsString());}catch(RestClientException e){release(reserved);throw new ApiException(503,"Product Service unavailable");}}
  // 3. Persist a pending aggregate before invoking payment.
  Order order=new Order(id,request.customerName(),total,"PENDING_PAYMENT",request.payment().cardNumber().substring(12));order.getItems().addAll(lines);orders.save(order);
  // 4. Call the SOAP payment contract.
  try { ProcessPaymentResponse response=payments.process(id,total,request.payment().method(),request.payment().cardNumber());
   if("APPROVED".equals(response.getStatus())){order.setStatus("CONFIRMED");order.setTransactionId(response.getTransactionId());return orders.save(order);}
   release(reserved);order.setStatus("PAYMENT_FAILED");order.setFailureReason(response.getMessage());return orders.save(order);
  } catch(org.springframework.ws.soap.client.SoapFaultClientException e){release(reserved);throw new ApiException(502,"Payment Service fault: "+e.getFaultStringOrReason());}
    catch(WebServiceIOException e){release(reserved);throw new ApiException(503,"Payment Service unavailable");}
 }
 @Transactional public Order cancel(String id){Order o=orders.findById(id).orElseThrow(()->new ApiException(404,"Order "+id+" not found"));if(!"CONFIRMED".equals(o.getStatus()))throw new ApiException(409,"Only CONFIRMED orders can be cancelled");try{payments.refund(o.getTransactionId());}catch(org.springframework.ws.soap.client.SoapFaultClientException e){throw new ApiException(502,"Payment Service fault: "+e.getFaultStringOrReason());}catch(WebServiceIOException e){throw new ApiException(503,"Payment Service unavailable");}for(OrderItem i:o.getItems())releaseOne(i.getProductId(),i.getQuantity());o.setStatus("CANCELLED");return orders.save(o);}
 public List<Order> all(){return orders.findAll();} public Order one(String id){return orders.findById(id).orElseThrow(()->new ApiException(404,"Order "+id+" not found"));}
 private void release(List<OrderRequest.Item> items){for(var i:items)releaseOne(i.productId(),i.quantity());}
 private void releaseOne(long id,int q){try{products.release(id,q);}catch(RestClientException e){log.warn("[ORDER] Stock compensation failed for product {}",id);}}
 private String random(int n){String chars="0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";StringBuilder b=new StringBuilder(n);for(int i=0;i<n;i++)b.append(chars.charAt(ThreadLocalRandom.current().nextInt(chars.length())));return b.toString();}
}
