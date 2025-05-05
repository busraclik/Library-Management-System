package com.busra.library.controller;

import com.busra.library.model.dto.UserDTO;
import com.busra.library.model.dto.UserRequest;
import com.busra.library.model.dto.UserResponse;
import com.busra.library.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> save(@Valid @RequestBody UserDTO userDTO){
        return ResponseEntity.ok(authenticationService.save(userDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> auth(@Valid @RequestBody UserRequest userRequest){
        return ResponseEntity.ok(authenticationService.auth(userRequest));
    }


}
