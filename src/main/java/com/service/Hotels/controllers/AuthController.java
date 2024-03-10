package com.service.Hotels.controllers;
import com.service.Hotels.services.AuthService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.Hotels.controllers.Requests.AuthResponse;
import com.service.Hotels.controllers.Requests.LoginRequest;
import com.service.Hotels.controllers.Requests.RegisterRequest;

import lombok.RequiredArgsConstructor;

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
  public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request)
  {
    return ResponseEntity.ok(authService.register(request));
  }
  
  
}
