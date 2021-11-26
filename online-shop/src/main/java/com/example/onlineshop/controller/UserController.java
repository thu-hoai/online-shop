package com.example.onlineshop.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.onlineshop.constants.WebContants;
import com.example.onlineshop.dto.UserDto;
import com.example.onlineshop.security.dto.JwtUser;
import com.example.onlineshop.service.facade.OrderPlaceFacade;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping(WebContants.USERS)
@AllArgsConstructor
public class UserController {

	private final OrderPlaceFacade service;

	@GetMapping
	@PreAuthorize("hasAuthority('Administrator')")
	public List<JwtUser> getAllUsers() {
		return service.findAllUsers();
	}

	@GetMapping(value = "/{id}")
	@PreAuthorize("hasAuthority('Administrator')")
	public JwtUser getUserById(@PathVariable Long id) {
		return service.findUserById(id);
	}

	@PostMapping
	public JwtUser createUser(@RequestBody UserDto user) {
		return service.createUser(user);
	}

	@PutMapping
	public JwtUser updateUser(@RequestBody UserDto user) {
		return service.updateUser(user);
	}

	@DeleteMapping(value = "/{id}")
	@PreAuthorize("hasAuthority('Administrator')")
	public void deleteUser(@PathVariable Long id) {
		service.deleteUserById(id);
	}

}
