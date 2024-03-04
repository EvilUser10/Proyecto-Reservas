package com.service.Hotels.exceptions;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String msg){
        super(msg);
    }
}
