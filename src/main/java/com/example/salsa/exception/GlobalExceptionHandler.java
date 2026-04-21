package com.example.salsa.exception;

import com.example.salsa.response.WebResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<WebResponse<Object>> handlerRuntime(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                WebResponse.<Object>builder()
                        .status("error")
                        .message(ex.getMessage())
                        .data(null)
                        .build()
        );


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