package com.example.onlineshop.service;

import java.util.List;

import com.example.onlineshop.dto.UserDto;
import com.example.onlineshop.security.dto.JwtUser;

public interface UserService {

	JwtUser findUserById(Long id);

	JwtUser createUser(UserDto userDto);

	List<JwtUser> findAllUsers();

	void deleteUserById(Long id);

	JwtUser updateUser(UserDto userDto);

}
