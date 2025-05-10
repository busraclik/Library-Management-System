package com.busra.library.service.impl;

import com.busra.library.service.JwtService;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceImplTest {

    private JwtServiceImpl jwtService;
    private final String secretKey = "my-256-bit-secret-my-256-bit-secret";

    private UserDetails userDetails;

    @BeforeEach
    void setUp() throws Exception {
        jwtService = new JwtServiceImpl();

        // secret key'i manuel ayarlıyoruz çünkü @Value testte çalışmaz
        Field field = JwtServiceImpl.class.getDeclaredField("SECRET_KEY");
        field.setAccessible(true);
        field.set(jwtService, java.util.Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8)));

        userDetails = new User("testuser", "password", List.of(() -> "ROLE_USER"));
    }

    @Test
    void generateToken_ShouldReturnValidJwt() {
        String token = jwtService.generateToken(userDetails);
        assertNotNull(token);
        assertTrue(token.startsWith("ey")); // JWT genelde 'eyJ...' ile başlar
    }

    @Test
    void findUsername_ShouldExtractCorrectUsername() {
        String token = jwtService.generateToken(userDetails);
        String username = jwtService.findUsername(token);
        assertEquals("testuser", username);
    }

    @Test
    void tokenControl_ShouldReturnTrueForValidToken() {
        String token = jwtService.generateToken(userDetails);
        boolean isValid = jwtService.tokenControl(token, userDetails);
        assertTrue(isValid);
    }

    @Test
    void extractRole_ShouldReturnCorrectRole() {
        String token = jwtService.generateToken(userDetails);
        String role = jwtService.extractRole(token);
        assertEquals("ROLE_USER", role);
    }
}
