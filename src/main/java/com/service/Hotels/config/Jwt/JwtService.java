package com.service.Hotels.config.Jwt;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.util.HashMap;
import java.util.Map;
import java.security.Key;
import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;

@Service
public class JwtService {

  private static String SECRET_KEY = "586E3272357538782F413F4428472B4B6250655368566B597033733676397924";
  
  public String getToken(UserDetails user) {
    return getToken(new HashMap<>(), user);
  }

  private String getToken(Map<String, Object> extraClaims, UserDetails user) {
    return Jwts
    .builder()
    .setClaims(extraClaims)
    .setSubject(user.getUsername())
    .setIssuedAt(new Date(System.currentTimeMillis()))
    .setExpiration(new Date(System.currentTimeMillis()+1000*60*24))
    .signWith(getKey(), SignatureAlgorithm.HS256)
    .compact();
  }

  private Key getKey(){
    byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
