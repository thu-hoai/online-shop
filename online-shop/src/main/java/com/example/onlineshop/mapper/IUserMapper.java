package com.example.onlineshop.mapper;

import org.mapstruct.Mapper;

import com.example.onlineshop.entity.User;
import com.example.onlineshop.security.dto.JwtUser;

@Mapper
public interface IUserMapper {

	JwtUser convertToDto(User user);
	
}
