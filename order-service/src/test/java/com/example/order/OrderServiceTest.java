package com.example.order;

import com.example.order.client.*; import com.example.order.dto.*; import com.example.order.entity.*; import com.example.order.repository.OrderRepository; import com.example.order.service.OrderService; import com.example.order.soap.ProcessPaymentResponse; import java.math.*; import java.util.*; import static org.junit.jupiter.api.Assertions.*; import static org.mockito.ArgumentMatchers.*; import static org.mockito.Mockito.*; import org.junit.jupiter.api.Test;

/** Focused orchestration tests using mocked downstream services. */
class OrderServiceTest {
  private final ProductClient products=mock(ProductClient.class); private final PaymentClient payments=mock(PaymentClient.class); private final OrderRepository repository=mock(OrderRepository.class); private final OrderService service=new OrderService(products,payments,repository);
  private OrderRequest request(){return new OrderRequest("Asha",List.of(new OrderRequest.Item(1L,2)),new OrderRequest.Payment("CARD","4111111111111111"));}
  @Test void happyPathConfirmsAndReserves(){when(products.get(1)).thenReturn(new ProductView(1L,"Mouse","accessories",BigDecimal.TEN,5));doNothing().when(products).reserve(1,2);var response=new ProcessPaymentResponse();response.setStatus("APPROVED");response.setTransactionId("TXN-1");when(payments.process(anyString(),any(),anyString(),anyString())).thenReturn(response);when(repository.save(any())).thenAnswer(i->i.getArgument(0));assertEquals("CONFIRMED",service.place(request()).getStatus());verify(products).reserve(1,2);}
  @Test void declinedReleasesStock(){when(products.get(1)).thenReturn(new ProductView(1L,"Mouse","accessories",BigDecimal.TEN,5));var response=new ProcessPaymentResponse();response.setStatus("DECLINED");response.setMessage("Card declined by issuer");when(payments.process(anyString(),any(),anyString(),anyString())).thenReturn(response);when(repository.save(any())).thenAnswer(i->i.getArgument(0));assertEquals("PAYMENT_FAILED",service.place(request()).getStatus());verify(products).release(1,2);}
}
