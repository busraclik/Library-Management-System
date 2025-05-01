package com.busra.library.service;

import com.busra.library.model.dto.UserDTO;
import com.busra.library.model.dto.UserRequest;
import com.busra.library.model.dto.UserResponse;

public interface AuthenticationService {
    UserResponse save(UserDTO userDTO);

    UserResponse auth(UserRequest userRequest);
}
