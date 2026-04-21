package com.example.salsa.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class WebResponse<T>{
    private String status;
    private String message;
    private T data;
}
