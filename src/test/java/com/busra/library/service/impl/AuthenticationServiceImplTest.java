package com.busra.library.service.impl;

import com.busra.library.model.dto.UserRequestDTO;
import com.busra.library.model.dto.request.UserRequest;
import com.busra.library.model.dto.response.UserResponse;
import com.busra.library.model.entity.User;
import com.busra.library.model.enums.Role;
import com.busra.library.repository.UserRepository;
import com.busra.library.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationServiceImplTest {

    private AuthenticationServiceImpl authService;
    private UserRepository userRepository;
    private JwtService jwtService;
    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        jwtService = mock(JwtService.class);
        authenticationManager = mock(AuthenticationManager.class);
        passwordEncoder = mock(PasswordEncoder.class);

        authService = new AuthenticationServiceImpl(userRepository, jwtService, authenticationManager, passwordEncoder);
    }

    @Test
    void testSave_shouldReturnUserResponseWithToken() {
        UserRequestDTO dto = UserRequestDTO.builder()
                .username("busra")
                .password("pass123")
                .nameSurname("Busra K")
                .role(Role.valueOf("LIBRARIAN"))
                .build();

        when(passwordEncoder.encode("pass123")).thenReturn("encodedPass");
        when(jwtService.generateToken(any(User.class))).thenReturn("mocked-jwt-token");

        UserResponse response = authService.save(dto);

        assertNotNull(response);
        assertEquals("mocked-jwt-token", response.getToken());

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        assertEquals("busra", userCaptor.getValue().getUsername());
        assertEquals("encodedPass", userCaptor.getValue().getPassword());
    }

    @Test
    void testAuth_shouldReturnUserResponseWithToken() {
        UserRequest request = UserRequest.builder()
                .username("busra")
                .password("pass123")
                .build();

        User user = User.builder()
                .username("busra")
                .password("encodedPass")
                .role(Role.valueOf("PATRON"))
                .build();

        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(null); // Sadece başarılı olsun yeterli
        when(userRepository.findByUsername("busra")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("token123");

        UserResponse response = authService.auth(request);

        assertNotNull(response);
        assertEquals("token123", response.getToken());
    }

    @Test
    void testAuth_shouldThrowExceptionWhenUserNotFound() {
        UserRequest request = UserRequest.builder()
                .username("invalid")
                .password("pass123")
                .build();

        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(null);
        when(userRepository.findByUsername("invalid")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> authService.auth(request));
        assertTrue(exception.getMessage().contains("Authentication failed"));
    }
}
