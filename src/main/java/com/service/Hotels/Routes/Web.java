package com.service.Hotels.Routes;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Web {
    
    @GetMapping("/")
    public String miMetodo() {
        return "Hello World";
    }
}
