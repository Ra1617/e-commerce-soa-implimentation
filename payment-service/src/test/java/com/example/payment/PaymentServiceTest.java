package com.example.payment;

import com.example.payment.entity.PaymentTransaction;
import com.example.payment.exception.PaymentFaultException;
import com.example.payment.repository.PaymentTransactionRepository;
import com.example.payment.service.PaymentService;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;

/** Unit coverage for the payment rules without starting the SOAP server. */
class PaymentServiceTest {
  private final PaymentTransactionRepository repository=mock(PaymentTransactionRepository.class);
  private final PaymentService service=new PaymentService(repository);
  @Test void approvesNormalCard(){when(repository.save(any())).thenAnswer(i->i.getArgument(0));assertEquals("APPROVED",service.process("O",BigDecimal.ONE,"CARD","4111111111111111").getStatus());}
  @Test void declinesEndingInZeros(){when(repository.save(any())).thenAnswer(i->i.getArgument(0));assertEquals("DECLINED",service.process("O",BigDecimal.ONE,"CARD","4111111111110000").getStatus());}
  @Test void rejectsInvalidCard(){assertThrows(PaymentFaultException.class,()->service.process("O",BigDecimal.ONE,"CARD","1234"));}
  @Test void refundRequiresApproved(){when(repository.findById("x")).thenReturn(java.util.Optional.of(new PaymentTransaction("x","o",BigDecimal.ONE,"CARD","1111","DECLINED")));assertThrows(PaymentFaultException.class,()->service.refund("x"));}
  @Test void refundApproved(){var t=new PaymentTransaction("x","o",BigDecimal.ONE,"CARD","1111","APPROVED");when(repository.findById("x")).thenReturn(java.util.Optional.of(t));when(repository.save(t)).thenReturn(t);assertEquals("REFUNDED",service.refund("x").getStatus());}
}
