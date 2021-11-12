package com.example.onlineshop.dto;

import java.util.Collection;

import com.example.onlineshop.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
	private Long id;

	private String username;

	private String firstName;

	private String lastName;

	private String email;

	private String phone;

	private String address;
	private String password;

	private Collection<Role> authorities;
}
