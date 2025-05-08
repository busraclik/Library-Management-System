package com.busra.library.service;

import com.busra.library.model.dto.UserRequestDTO;
import com.busra.library.model.dto.request.UserRequest;
import com.busra.library.model.dto.response.UserResponse;

public interface AuthenticationService {
    UserResponse save(UserRequestDTO userRequestDTO);

    UserResponse auth(UserRequest userRequest);
}
