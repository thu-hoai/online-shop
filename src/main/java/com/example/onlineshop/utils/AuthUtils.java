package com.example.onlineshop.utils;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.onlineshop.security.dto.JwtUser;
import com.example.onlineshop.service.exception.AccessDeniedException;

public class AuthUtils {

	/* Avoid to create an instance */
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

	public static JwtUser getAuthorizedUser(String username) {
		JwtUser principal = (JwtUser) getCurrentLoggedInUserDetails().getPrincipal();
		if (!principal.getUsername().equals(username)) {
			throw new AccessDeniedException(UNAUTHORIZED_MESSAGE);
		}
		return principal;
	}

	public static JwtUser getAuthorizedUser(Long id) {
		JwtUser principal = (JwtUser) getCurrentLoggedInUserDetails().getPrincipal();
		if (!principal.getId().equals(id)) {
			throw new AccessDeniedException(UNAUTHORIZED_MESSAGE);
		}
		return principal;
	}
}
