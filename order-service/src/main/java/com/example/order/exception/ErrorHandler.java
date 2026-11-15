package com.example.order.exception;
import jakarta.servlet.http.HttpServletRequest; import java.time.Instant; import java.util.Map; import org.springframework.web.bind.annotation.*; import org.springframework.http.ResponseEntity;
/** Consistent error envelope for REST clients. */
@RestControllerAdvice public class ErrorHandler {
 @ExceptionHandler(ApiException.class) public ResponseEntity<Map<String,Object>> api(ApiException e,HttpServletRequest r){return ResponseEntity.status(e.getStatus()).body(Map.of("timestamp",Instant.now(),"status",e.getStatus(),"error",e.getStatus()==404?"Not Found":e.getStatus()==409?"Conflict":"Service Error","message",e.getMessage(),"path",r.getRequestURI()));}
 @ExceptionHandler(Exception.class) public ResponseEntity<Map<String,Object>> other(Exception e,HttpServletRequest r){return ResponseEntity.status(500).body(Map.of("timestamp",Instant.now(),"status",500,"error","Internal Server Error","message",e.getMessage(),"path",r.getRequestURI()));}
}
