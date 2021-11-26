package com.example.onlineshop.service.impl;

import com.example.onlineshop.dto.UserDto;
import com.example.onlineshop.entity.Role;
import com.example.onlineshop.entity.User;
import com.example.onlineshop.enums.ExceptionCode;
import com.example.onlineshop.enums.UserRole;
import com.example.onlineshop.repository.UserRepository;
import com.example.onlineshop.security.dto.JwtUser;
import com.example.onlineshop.service.UserService;
import com.example.onlineshop.service.exception.ExistentUserException;
import com.example.onlineshop.service.exception.UserNotFoundException;
import com.example.onlineshop.utils.ErrorMessageFormatUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public List<JwtUser> findAllUsers() {
        List<User> users = repository.findAll();
        return users.stream().map(JwtUser::build).collect(Collectors.toList());
    }

    @Override
    public void deleteUserById(Long id) {
        User existentUser = repository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        repository.deleteById(existentUser.getId());

    }

    @Override
    public JwtUser updateUser(UserDto userDto) {
        User existentUser = repository.findById(userDto.getId())
                .orElseThrow(() -> new UserNotFoundException(ErrorMessageFormatUtils.getErrorMessage(ExceptionCode.USER_NOT_FOUND, userDto.getId().toString())));

        User updatedUser = repository.save(updateUser(existentUser, userDto));

        return JwtUser.build(updatedUser);

    }

    @Override
    public JwtUser createUser(UserDto userDto) {

        User existentUser = repository.findByUsername(userDto.getUsername());
        if (existentUser != null) {
            throw new ExistentUserException(ErrorMessageFormatUtils.getErrorMessage(ExceptionCode.EXISTENT_USER, userDto.getUsername()));
        }
        User entity = new User();
        entity.setUsername(userDto.getUsername());
        User updatedUser = repository.save(updateUser(entity, userDto));
        return JwtUser.build(updatedUser);
    }

    private User updateUser(User entity, UserDto userDto) {

        entity.setEmail(userDto.getEmail());
        entity.setFirstName(userDto.getFirstName());
        entity.setLastName(userDto.getLastName());
        entity.setEnabled(true);
        entity.setPassword(passwordEncoder.encode(userDto.getPassword()));

        if (userDto.getAuthorities() != null) {
            entity.setAuthorities(userDto.getAuthorities().stream().map(role -> {
                String roleName = role.getName();
                Role temp = new Role();
                temp.setId(UserRole.valueOf(roleName.toUpperCase()).getRoleId());
                temp.setName(roleName);
                return temp;
            }).collect(Collectors.toSet()));
        }

        return entity;

    }

    @Override
    public JwtUser findUserById(Long id) {
        User entity = repository.findById(id).orElseThrow(
                () ->  new UserNotFoundException(ErrorMessageFormatUtils.getErrorMessage(ExceptionCode.USER_NOT_FOUND, id.toString())));
        return JwtUser.build(entity);
    }


}
