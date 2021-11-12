package com.example.onlineshop.service.exception;

public class ExistentUserException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ExistentUserException(String message) {
		super(message);
	}

}
