package com.busra.library.model.mapper;


import com.busra.library.model.dto.UserDTO;
import com.busra.library.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO userToUserDTO(User user);
    User userDtoToUser(UserDTO userDTO);
}
