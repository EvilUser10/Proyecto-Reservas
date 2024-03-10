package com.service.Hotels.services;

import org.springframework.stereotype.Service;

import com.service.Hotels.config.Jwt.JwtService;
import com.service.Hotels.controllers.Requests.AuthResponse;
import com.service.Hotels.controllers.Requests.LoginRequest;
import com.service.Hotels.controllers.Requests.RegisterRequest;
import com.service.Hotels.interfaces.Role;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.service.Hotels.repositories.UserRepository;
import com.service.Hotels.models.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;

  public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        UserDetails user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        String token=jwtService.getToken(user);
        return AuthResponse.builder()
            .token(token)
            .build();

    }

  public AuthResponse register(RegisterRequest request){
    User user = User.builder()
    .username(request.getUsername())
    .password(passwordEncoder.encode(request.getPassword()))
    .email(request.getEmail())
    .role(Role.USER)
    .build();

    userRepository.save(user);

    return AuthResponse.builder()
    .token(jwtService.getToken(user))
    .build();
  }
}
