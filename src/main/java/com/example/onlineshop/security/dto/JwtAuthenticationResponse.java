package com.example.onlineshop.security.dto;

import org.springframework.security.core.userdetails.UserDetails;

public class JwtAuthenticationResponse {
	private String token;

	private UserDetails user;

	public JwtAuthenticationResponse(String token, UserDetails user) {
		this.token = token;
		this.user = user;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public UserDetails getUser() {
		return user;
	}

	public void setUser(UserDetails user) {
		this.user = user;
	}
}
