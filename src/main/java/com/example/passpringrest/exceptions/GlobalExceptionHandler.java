package com.example.passpringrest.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON)
                .body(exception.getMessage());
    }

    @ExceptionHandler(ResourceOccupiedException.class)
    public ResponseEntity<Object> handleResourceOccupiedException(ResourceOccupiedException exception) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT).contentType(MediaType.APPLICATION_JSON)
                .body(exception.getMessage());
    }

    @ExceptionHandler(RentTransactionException.class)
    public ResponseEntity<Object> handleRentTransactionException(RentTransactionException exception) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT).contentType(MediaType.APPLICATION_JSON)
                .body(exception.getMessage());
    }

}
