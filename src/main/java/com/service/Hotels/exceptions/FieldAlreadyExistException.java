package com.service.Hotels.exceptions;

public class FieldAlreadyExistException extends ConflictException {
    public FieldAlreadyExistException(String msg){
        super(msg);
    }
}
