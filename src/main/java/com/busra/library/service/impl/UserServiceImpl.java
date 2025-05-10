package com.busra.library.service.impl;

import com.busra.library.exception.UserNotFoundException;
import com.busra.library.model.dto.UserRequestDTO;
import com.busra.library.model.dto.UserResponseDTO;
import com.busra.library.model.entity.User;
import com.busra.library.model.mapper.UserMapper;
import com.busra.library.repository.UserRepository;
import com.busra.library.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::userToUserResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        return userMapper.userToUserResponseDTO(user);
    }

    @Override
    public UserResponseDTO updateUser(Long id, UserRequestDTO userRequestDTO) {
        User user = userMapper.userRequestDtoToUser(userRequestDTO);
        user.setId(id);
        User saveUser = userRepository.save(user);
        return userMapper.userToUserResponseDTO(saveUser);
    }


//    @Override
//    public void deleteUser(Long id) {
//        userRepository.deleteById(id);
//    }

    public void deleteUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        userRepository.delete(user);
    }


    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
