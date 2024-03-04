package com.service.Hotels.exceptions;

public class MalformedHeaderException extends BadRequestException {
    public MalformedHeaderException(String msg){
        super(msg);
    }
}
