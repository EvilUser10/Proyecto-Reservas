package com.service.Hotels.exceptions;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String msg){
        super(msg);
    }
    
}
