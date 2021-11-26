package com.example.onlineshop.service.exception;

public class ProductNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ProductNotFoundException(String exp, Throwable e) {
		super(exp, e);

	}

	public ProductNotFoundException(String exp) {
		super(exp);

	}
}
