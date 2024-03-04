package com.service.Hotels.exceptions;

public class FieldInvalidException extends BadRequestException {
    public FieldInvalidException(String msg){
        super(msg);
    }
    
}
