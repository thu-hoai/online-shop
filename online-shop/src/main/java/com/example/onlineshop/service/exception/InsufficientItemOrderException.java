package com.example.onlineshop.service.exception;

public class InsufficientItemOrderException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InsufficientItemOrderException(String exp, Throwable e) {
		super(exp, e);
	}
	
	public InsufficientItemOrderException(String exp) {
		super(exp);
	}
}
