package com.example.onlineshop.service.exception;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

/**
 * Represent a custom exception response
 */
@Getter
@Setter
public class ExceptionResponse {
	/** The status code of the response */
	private String status;

	/** The timestamp */
	private Instant timestamp;

	/** The message to show of the response */
	private String message;

	/** The description. */
	private String description;

	/** The short error code */
	private String errorCode;

	/**
	 * Instantiates a new exception response.
	 *
	 * @param errorMessage the error message
	 * @param statusCode   the status code
	 * @param description  the description
	 */
	public ExceptionResponse(String errorMessage, String statusCode, String description, String errorCode) {
		super();
		this.message = errorMessage;
		this.status = statusCode;
		this.description = description;
		this.errorCode = errorCode;
		this.timestamp = Instant.now();
	}
}
