package com.zerooversell.reservation.controller;

import com.zerooversell.reservation.dto.ApiError;
import com.zerooversell.reservation.service.DuplicateCustomerReservationException;
import com.zerooversell.reservation.service.OutOfStockException;
import com.zerooversell.reservation.service.ReservationNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(OutOfStockException.class)
    ResponseEntity<ApiError> outOfStock() {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiError("OUT_OF_STOCK"));
    }

    @ExceptionHandler(DuplicateCustomerReservationException.class)
    ResponseEntity<ApiError> duplicateReservation() {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiError("CUSTOMER_ALREADY_RESERVED"));
    }

    @ExceptionHandler(ReservationNotFoundException.class)
    ResponseEntity<ApiError> reservationNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiError("RESERVATION_NOT_FOUND"));
    }
}
