package com.gogitek.toeictest.config.exception;

import com.gogitek.toeictest.config.response.ResponseEntityBuilder;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = ToeicRuntimeException.class)
    public ResponseEntity<?> handler(ToeicRuntimeException e, HttpServletRequest request) {
        return ResponseEntityBuilder
                .getBuilder()
                .setCode(Integer.parseInt(e.getCode()))
                .setMessage(e.getMessage())
                .build();
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handler(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        return ResponseEntityBuilder
                .getBuilder()
                .setCode(e.hashCode())
                .setMessage(e.getMessage())
                .build();
    }
}
