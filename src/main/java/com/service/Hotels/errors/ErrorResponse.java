package com.service.Hotels.errors;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private String exception ;
    private Integer code;
    private HttpStatus status;
    private String message;
    private String path;
    //private Exception e;

    public ErrorResponse(HttpStatus status, Exception ex, String path){
        this.exception = ex.getClass().getSimpleName();//Para ver el nombre de la exception lanzada.
        this.code = status.value();
        this.status = status;
        this.message = ex.getMessage();
        this.path = path;
        //this.e = ex; //Para ver todas los detalles de la exception lanzada.
    }
    

}
