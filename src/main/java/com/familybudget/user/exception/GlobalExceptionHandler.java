package com.familybudget.user.exceptionhandler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return ResponseEntity.internalServerError().body(ex.getBindingResult().getAllErrors().getFirst().getDefaultMessage());
    }
    @ExceptionHandler(FamilyException.class)
    public ResponseEntity<?> handleFamilyException(FamilyException ex) {
        return ResponseEntity.internalServerError().body(ex.getMessage());
    }
}
