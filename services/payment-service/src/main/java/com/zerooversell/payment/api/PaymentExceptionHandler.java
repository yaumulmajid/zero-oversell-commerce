package com.zerooversell.payment.api;

import com.zerooversell.payment.service.PaymentExpiredException;
import com.zerooversell.payment.service.PaymentNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Map;

@RestControllerAdvice
public class PaymentExceptionHandler {
    @ExceptionHandler(PaymentNotFoundException.class)
    ResponseEntity<Map<String, String>> notFound() { return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("code", "PAYMENT_NOT_FOUND")); }
    @ExceptionHandler(PaymentExpiredException.class)
    ResponseEntity<Map<String, String>> expired() { return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("code", "PAYMENT_EXPIRED")); }
}
