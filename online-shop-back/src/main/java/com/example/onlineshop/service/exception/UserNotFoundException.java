package com.example.onlineshop.service.exception;

public class UserNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UserNotFoundException(String exp, Throwable e) {
		super(exp, e);

	}
	
	public UserNotFoundException(String exp) {
		super(exp);

	}
}
