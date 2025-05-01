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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;


    @Override
    public UserResponse save(UserDTO userDTO) {
        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
        User user = User.builder().userName(userDTO.getUserName())
                .password(encodedPassword).nameSurname(userDTO.getNameSurname())
                .role(Role.LIBRARIAN).build();

        userRepository.save(user);

        var token = jwtService.generateToken(user);
        return UserResponse.builder().token(token).build();
    }

    @Override
    public UserResponse auth(UserRequest userRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userRequest.getUserName(), userRequest.getPassword()));
            User user = userRepository.findByUserName(userRequest.getUserName()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            String token = jwtService.generateToken(user);
            return UserResponse.builder().token(token).build();
        }catch (Exception e) {
            throw new RuntimeException("Authentication failed: " + e.getMessage());
        }
    }
}
