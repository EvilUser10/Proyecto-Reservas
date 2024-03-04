package com.service.Hotels.exceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String msg){
        super(msg);
    }
    
}
