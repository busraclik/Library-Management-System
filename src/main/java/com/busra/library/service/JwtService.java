package com.busra.library.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String findUsername(String token);

    boolean tokenControl(String jwt, UserDetails userDetails);
    String extractRole(String token);

    String generateToken(UserDetails user);
}
