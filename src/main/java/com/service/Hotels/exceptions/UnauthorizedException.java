package com.service.Hotels.exceptions;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String msg){
        super(msg);
    }
    
}
