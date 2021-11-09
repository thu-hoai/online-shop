package com.example.onlineshop.service.exception;

public class OrderNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public OrderNotFoundException(String exp, Throwable e) {
		super(exp, e);
	}

	public OrderNotFoundException(String exp) {
		super(exp);
	}
}
