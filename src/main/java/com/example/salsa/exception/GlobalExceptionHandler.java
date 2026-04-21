package com.example.salsa.exception;

import com.example.salsa.response.WebResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public WebResponse<String> handleNotFound(NotFoundException ex) {
        return WebResponse.<String>builder()
                .status("Fail")
                .message(ex.getMessage())
                .data(null)
                .build();
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public WebResponse<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return WebResponse.<Map<String, String>>builder()
                .status("Fail")
                .message("Validasi gagal")
                .data(errors)
                .build();
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public WebResponse<String> handleGeneral(Exception e) {
        return WebResponse.<String>builder()
                .status("Error")
                .message(e.getMessage())
                .data(null)
                .build();
    }
}