package com.example.onlineshop.utils;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.onlineshop.security.dto.JwtUser;
import com.example.onlineshop.service.exception.AccessDeniedException;

public class AuthUtils {

	private AuthUtils() {
	}

	public static final String UNAUTHORIZED_MESSAGE = "Unauthorized to access this user";

	/**
	 * Return the currently authenticated principal
	 */

	public static Authentication getCurrentLoggedInUserDetails() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			throw new AccessDeniedException(UNAUTHORIZED_MESSAGE);
		}
		return authentication;
	}
	
	public static JwtUser getCurrentUserLoggedIn() {
		return (JwtUser) getCurrentLoggedInUserDetails().getPrincipal();
	}

}
