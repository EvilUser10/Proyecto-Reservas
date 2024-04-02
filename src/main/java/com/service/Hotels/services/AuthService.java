package com.service.Hotels.services;

import org.springframework.stereotype.Service;
import com.service.Hotels.config.Jwt.JwtService;
import com.service.Hotels.controllers.Requests.AuthResponse;
import com.service.Hotels.controllers.Requests.LoginRequest;
import com.service.Hotels.controllers.Requests.RegisterRequest;
import com.service.Hotels.exceptions.BadRequestException;
import com.service.Hotels.exceptions.FieldAlreadyExistException;
import com.service.Hotels.exceptions.NotFoundException;
import com.service.Hotels.interfaces.Role;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
    UserDetails user = userRepository.findByUsername(request.getUsername())
        .orElseThrow(() -> new NotFoundException("The user " + request.getUsername() + " does not exist!"));
    String token = jwtService.getToken(user);
    return AuthResponse.builder()
        .token(token)
        .build();
  }

  public User register(RegisterRequest request) {

    if (request.getUsername().length() == 0 || request.getUsername() == null) {
      throw new BadRequestException(request.getUsername() + " is not valid.");
    }

    if (request.getEmail().length() == 0 || request.getEmail() == null) {
      throw new BadRequestException(request.getUsername() + " is not valid.");
    }

    if (request.getPassword().length() == 0 || request.getPassword() == null) {
      throw new BadRequestException(request.getUsername() + " is not valid.");
    }

    if (request.getUsername().length() == 0 || request.getUsername() == null) {
      throw new BadRequestException(request.getUsername() + " is not valid.");
    }

    if (userRepository.existsByEmail(request.getEmail())) {
      throw new FieldAlreadyExistException(request.getEmail() + " already exists");
    }
    if (userRepository.existsByUsername(request.getUsername())) {
      throw new FieldAlreadyExistException(request.getUsername() + " already exists");
    }
    if (!validateEmail(request.getEmail())) {
      throw new BadRequestException(request.getEmail() + " is not valid.");
    }

    User user = User.builder()
        .username(request.getUsername())
        .password(passwordEncoder.encode(request.getPassword()))
        .email(request.getEmail())
        .role(Role.USER)
        .build();

    return userRepository.save(user);
  }

  private boolean validateEmail(String email) {
    String regexEmail = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";
    Pattern pattern = Pattern.compile(regexEmail);
    Matcher matcher = pattern.matcher(email);
    return matcher.matches();
  }
}
