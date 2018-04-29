package com.github.damiox.ecommerce.api;

import com.github.damiox.ecommerce.exception.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionResolver {

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<?> resolveNotFoundException() {
        return ResponseEntity.notFound().build();
    }

}
