package com.busra.library.service;

import com.busra.library.model.dto.UserRequestDTO;
import com.busra.library.model.dto.UserResponseDTO;
import com.busra.library.model.entity.User;


import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserResponseDTO> getAllUsers();

    UserResponseDTO getUserById(Long id);

    UserResponseDTO updateUser(Long id, UserRequestDTO userRequestDTO);

    void deleteUserById(Long id);

    Optional<User> findByUsername(String currentUsername);

    Long getUserIdByUsername(String username);
}
