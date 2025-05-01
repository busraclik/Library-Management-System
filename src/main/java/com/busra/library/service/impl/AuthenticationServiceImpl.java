package com.busra.library.service.impl;

import com.busra.library.model.dto.UserDTO;
import com.busra.library.model.dto.UserRequest;
import com.busra.library.model.dto.UserResponse;
import com.busra.library.model.entity.User;
import com.busra.library.model.enums.Role;
import com.busra.library.repository.UserRepository;
import com.busra.library.service.AuthenticationService;
import com.busra.library.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public UserResponse save(UserDTO userDTO) {
        User user = User.builder().userName(userDTO.getUserName())
                .password(userDTO.getPassword()).nameSurname(userDTO.getNameSurname())
                .role(Role.LIBRARIAN).build();

        userRepository.save(user);

        var token = jwtService.generateToken(user);
        return UserResponse.builder().token(token).build();
    }

    @Override
    public UserResponse auth(UserRequest userRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userRequest.getUserName(),userRequest.getPassword()));
        User user = userRepository.findByUserName(userRequest.getUserName()).orElseThrow();
        String token = jwtService.generateToken(user);
        return UserResponse.builder().token(token).build();
    }
}
