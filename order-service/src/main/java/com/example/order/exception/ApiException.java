package com.example.order.exception;
/** Explicit HTTP error used by the orchestrator and its exception handler. */
public class ApiException extends RuntimeException { private final int status; public ApiException(int status,String message){super(message);this.status=status;} public int getStatus(){return status;} }
