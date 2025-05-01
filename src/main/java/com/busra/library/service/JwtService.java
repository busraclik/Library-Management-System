package com.busra.library.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String findUserName(String token);

    boolean tokenControl(String jwt, UserDetails userDetails);

    String generateToken(UserDetails user);
}
