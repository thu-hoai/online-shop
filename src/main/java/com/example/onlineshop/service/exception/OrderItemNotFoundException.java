package com.example.onlineshop.service.exception;

public class OrderItemNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public OrderItemNotFoundException(String exp, Throwable e) {
		super(exp, e);
	}

	public OrderItemNotFoundException(String exp) {
		super(exp);
	}
}
