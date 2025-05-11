package com.busra.library.controller;

import com.busra.library.model.dto.UserRequestDTO;
import com.busra.library.model.dto.UserResponseDTO;
import com.busra.library.model.enums.Role;
import com.busra.library.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper = new ObjectMapper();

    private UserResponseDTO userResponseDTO;
    private UserRequestDTO userRequestDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        userResponseDTO = UserResponseDTO.builder()
                .id(1L)
                .username("busra")
                .role(Role.LIBRARIAN)
                .build();

        userRequestDTO = UserRequestDTO.builder()
                .username("busra")
                .password("securePassword") // En az 8 karakter
                .email("busra@example.com")
                .phoneNumber("1234567890")
                .nameSurname("Busra Celik")
                .role(Role.LIBRARIAN)
                .build();
    }

    @Test
    @WithMockUser(authorities = "LIBRARIAN")
    void getAllUsers_shouldReturnUserList() throws Exception {
        Mockito.when(userService.getAllUsers()).thenReturn(Collections.singletonList(userResponseDTO));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("busra"));
    }

    @Test
    @WithMockUser(authorities = "LIBRARIAN")
    void getUserById_shouldReturnUser() throws Exception {
        Mockito.when(userService.getUserById(1L)).thenReturn(userResponseDTO);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("busra"));
    }

    @Test
    @WithMockUser(authorities = "LIBRARIAN")
    void updateUser_shouldReturnUpdatedUser() throws Exception {
        Mockito.when(userService.updateUser(eq(1L), any(UserRequestDTO.class)))
                .thenReturn(userResponseDTO);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("busra"));
    }

    @Test
    @WithMockUser(authorities = "LIBRARIAN")
    void deleteUser_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(userService).deleteUserById(1L);
    }
}
