package com.busra.library.model.dto;

import com.busra.library.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private String nameSurname; //unique olmalı
    private String username;
    private Role role;
}
