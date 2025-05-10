package com.busra.library.service.impl;

import com.busra.library.exception.UsernameAlreadyExistsException;
import com.busra.library.model.dto.UserRequestDTO;
import com.busra.library.model.dto.request.UserRequest;
import com.busra.library.model.dto.response.UserResponse;
import com.busra.library.model.entity.User;
import com.busra.library.repository.UserRepository;
import com.busra.library.service.AuthenticationService;
import com.busra.library.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;


    @Override
    public UserResponse save(UserRequestDTO userRequestDTO) {

        boolean existsByUsername = userRepository.existsByUsername(userRequestDTO.getUsername());

        log.info("save attempt: {}  -- {}", userRequestDTO.getUsername(), existsByUsername);

        if (existsByUsername) {
            log.warn("Attempt to register with existing username: {}", userRequestDTO.getUsername());
            throw new UsernameAlreadyExistsException("Username '" + userRequestDTO.getUsername() + "' is already taken.");
        }

        String encodedPassword = passwordEncoder.encode(userRequestDTO.getPassword());
        User user = User.builder().username(userRequestDTO.getUsername())
                .password(encodedPassword)
                .nameSurname(userRequestDTO.getNameSurname())
                .phoneNumber(userRequestDTO.getPhoneNumber())
                .email(userRequestDTO.getEmail())
                .role(userRequestDTO.getRole()).build();

        userRepository.save(user);

        var token = jwtService.generateToken(user);
        return UserResponse.builder().token(token).build();
    }

    @Override
    public UserResponse auth(UserRequest userRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userRequest.getUsername(), userRequest.getPassword()));
            User user = userRepository.findByUsername(userRequest.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            String token = jwtService.generateToken(user);
            return UserResponse.builder().token(token).build();
        }catch (Exception e) {
            throw new RuntimeException("Authentication failed: " + e.getMessage());
        }
    }
}
