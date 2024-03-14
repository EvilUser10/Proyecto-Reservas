package com.service.Hotels.controllers;
import com.service.Hotels.services.AuthService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.Hotels.controllers.Requests.AuthResponse;
import com.service.Hotels.controllers.Requests.LoginRequest;
import com.service.Hotels.controllers.Requests.RegisterRequest;
import com.service.Hotels.exceptions.FieldAlreadyExistException;
import com.service.Hotels.models.User;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
  
  private final AuthService authService;


  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request)
  {
    return ResponseEntity.ok(authService.login(request));
  }
  
  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody RegisterRequest request)
  {
   try{
          authService.register(request);
          return ResponseEntity.ok("Registration successful!");

        }catch (FieldAlreadyExistException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
  }


  // @PostMapping("/register")
  // public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request)
  // {
  //   return ResponseEntity.ok(authService.register(request));
  // }
  
  
}
