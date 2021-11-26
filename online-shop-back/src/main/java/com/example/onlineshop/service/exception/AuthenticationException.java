package com.example.onlineshop.service.exception;

public class AuthenticationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AuthenticationException(String exp, Throwable e) {
		super(exp, e);

	}
}
