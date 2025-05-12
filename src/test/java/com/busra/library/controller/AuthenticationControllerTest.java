package com.busra.library.controller;

import com.busra.library.model.dto.UserRequestDTO;
import com.busra.library.model.dto.request.UserRequest;
import com.busra.library.model.dto.response.UserResponse;
import com.busra.library.model.enums.Role;
import com.busra.library.service.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class MockConfig {
        @Bean
        public AuthenticationService authenticationService() {
            return mock(AuthenticationService.class);
        }
    }

    @Test
    void testRegister_shouldReturnToken() throws Exception {
        UserRequestDTO requestDTO = UserRequestDTO.builder()
                .nameSurname("Busra Celik")
                .username("busra")
                .password("Password123")
                .role(Role.LIBRARIAN)
                .phoneNumber("05477777777")
                .email("busra@example.com")
                .build();

        UserResponse expectedResponse = UserResponse.builder()
                .token("mocked-jwt-token")
                .build();

        when(authenticationService.save(any(UserRequestDTO.class))).thenReturn(expectedResponse);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mocked-jwt-token"));
    }

    @Test
    void testLogin_shouldReturnToken() throws Exception {
        UserRequest loginRequest = UserRequest.builder()
                .username("busra")
                .password("Password123")
                .build();

        UserResponse expectedResponse = UserResponse.builder()
                .token("login-jwt-token")
                .build();

        when(authenticationService.auth(any(UserRequest.class))).thenReturn(expectedResponse);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("login-jwt-token"));
    }
}
