package com.busra.library.model.dto;

import com.busra.library.model.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {

    @NotBlank(message = "Name and surname are required")
    @Size(max = 100, message = "Name and surname should not exceed 100 characters")
    private String nameSurname;
    @NotBlank(message = "User name is required")
    @Size(max = 50, message = "Username should not exceed 50 characters")
    private String username;
    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    private String password;
    @NotNull(message = "Role is required")
    private Role role;
    @NotBlank(message = "Phone number is required")
    @Size(min = 10, max = 15, message = "Phone number must be between 10 and 15 characters")
    private String phoneNumber;
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
}
