package com.busra.library.model.mapper;


import com.busra.library.model.dto.UserRequestDTO;
import com.busra.library.model.dto.UserResponseDTO;
import com.busra.library.model.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User userRequestDtoToUser(UserRequestDTO userRequestDTO);
    UserResponseDTO userToUserResponseDTO(User user);
}

