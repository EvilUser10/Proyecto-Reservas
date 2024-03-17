package com.service.Hotels.exceptions;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
//import org.springframework.security.access.AccessDeniedException;
//TODO: Implementar cuando se añade la seguridad.
import com.service.Hotels.errors.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalException {

    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handlerUnexpectedException(Exception ex, HttpServletRequest request) {
       return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex, request.getRequestURI());
    }

    @ResponseBody
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handlerNotFoundException(Exception ex, HttpServletRequest request) {
       return new ErrorResponse(HttpStatus.NOT_FOUND, ex, request.getRequestURI());
    }

    @ResponseBody
    @ExceptionHandler({
      BadRequestException.class,
        DuplicateKeyException.class,
        HttpRequestMethodNotSupportedException.class,
        MethodArgumentNotValidException.class,
        MissingRequestHeaderException.class,
        MissingServletRequestParameterException.class,
        MethodArgumentTypeMismatchException.class,
        HttpMessageNotReadableException.class,
        BindException.class,
        IllegalArgumentException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerBadRequestException(Exception ex, HttpServletRequest request) {
       return new ErrorResponse(HttpStatus.BAD_REQUEST, ex, request.getRequestURI());
    }

    @ResponseBody
    @ExceptionHandler({ForbiddenException.class, AccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handlerForbiddenException(Exception ex, HttpServletRequest request) {
       return new ErrorResponse(HttpStatus.FORBIDDEN, ex, request.getRequestURI());
    }

    @ResponseBody
    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handlerConflictException(Exception ex, HttpServletRequest request) {
       return new ErrorResponse(HttpStatus.CONFLICT, ex, request.getRequestURI());
    }


    @ExceptionHandler({
        UnauthorizedException.class,
       //AccessDeniedException.class TODO: descomentar cuando se añade la seguridad
    })
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public void handlerUnauthorizedException() {
        //TODO: nothing to do here
    }
    
}
