package com.example.ExceptionHandler;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ExceptionResponse {
    private String message;
    private String details;

    public ExceptionResponse(String message,String details){
        this.message = message;
        this.details = details;
    }
}
