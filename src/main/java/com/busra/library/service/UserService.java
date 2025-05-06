package com.busra.library.service;

import com.busra.library.model.dto.UserDTO;
import com.busra.library.model.entity.User;


import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserDTO> getAllUsers();

    UserDTO getUserById(Long id);

    UserDTO updateUser(Long id, UserDTO userDTO);

    void deleteUser(Long id);

    Optional<User> findByUsername(String currentUsername);
}
