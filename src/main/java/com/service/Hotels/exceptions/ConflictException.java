package com.service.Hotels.exceptions;

public class ConflictException  extends RuntimeException {
    public ConflictException(String msg){
        super(msg);
    }
}
