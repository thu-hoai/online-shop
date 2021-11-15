package com.example.onlineshop.service.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.onlineshop.enums.ExceptionCode;

import lombok.extern.slf4j.Slf4j;

/**
 * Controller advice to translate the server side exceptions to client-friendly
 * json structures. 
 * 
 */
@RestControllerAdvice
@Slf4j
public class ExceptionTranslator extends ResponseEntityExceptionHandler {

	/**
	 * Not found exception handling
	 */

	@ExceptionHandler({ UserNotFoundException.class })
	public final ResponseEntity<ExceptionResponse> handleNonExistingElement(UserNotFoundException ex,
			WebRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage(),
				ExceptionCode.USER_NOT_FOUND.getStatusCode(),
				request.getDescription(false), ExceptionCode.USER_NOT_FOUND.toString());
		return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler({ AuthenticationException.class })
	public final ResponseEntity<ExceptionResponse> handleUnauthorizedException(AuthenticationException ex,
			WebRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage(),
				Integer.toString(HttpStatus.UNAUTHORIZED.value()), request.getDescription(false),
				HttpStatus.UNAUTHORIZED.name());
		return new ResponseEntity<>(exceptionResponse, HttpStatus.UNAUTHORIZED);
	}

	/**
	 * Bad request exception handling
	 */
	@ExceptionHandler({ BadRequestException.class })
	public final ResponseEntity<ExceptionResponse> handleBadRequestException(BadRequestException ex,
			WebRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage(),
				Integer.toString(HttpStatus.BAD_REQUEST.value()), request.getDescription(false),
				HttpStatus.BAD_REQUEST.name());
		return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({ InsufficientItemOrderException.class })
	public final ResponseEntity<ExceptionResponse> handleExistentEmailException(InsufficientItemOrderException ex,
			WebRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage(),
				ExceptionCode.OUT_OF_STOCK_ORDER_ITEM.getStatusCode(), request.getDescription(false),
				ExceptionCode.OUT_OF_STOCK_ORDER_ITEM.toString());
		return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({ OrderItemNotFoundException.class })
	public final ResponseEntity<ExceptionResponse> handleTakenUsernameException(OrderItemNotFoundException ex,
			WebRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage(),
				ExceptionCode.ORDER_ITEM_NOT_FOUND.getStatusCode(), request.getDescription(false),
				ExceptionCode.ORDER_ITEM_NOT_FOUND.toString());
		return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Access denied exception handling
	 */
	@ExceptionHandler({ AccessDeniedException.class })
	public final ResponseEntity<ExceptionResponse> handleAccessDeniedException(AccessDeniedException ex,
			WebRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getLocalizedMessage(), "403",
				request.getDescription(false), ExceptionCode.ERROR_ACCESS_DENIED.toString());
		return new ResponseEntity<>(exceptionResponse, HttpStatus.FORBIDDEN);
	}

	/**
	 * Non Exist Token Exception handling
	 */
	@ExceptionHandler({ OrderNotFoundException.class })
	public final ResponseEntity<ExceptionResponse> handleNonExistentTokenException(OrderNotFoundException ex,
			WebRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage(),
				Integer.toString(HttpStatus.BAD_REQUEST.value()), request.getDescription(false),
				ExceptionCode.ORDER_NOT_FOUND.toString());
		return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Non Exist Token Exception handling
	 */
	@ExceptionHandler({ ProductNotFoundException.class })
	public final ResponseEntity<ExceptionResponse> handleExpiredTokenException(ProductNotFoundException ex,
			WebRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage(),
				Integer.toString(HttpStatus.BAD_REQUEST.value()), request.getDescription(false),
				ExceptionCode.PRODUCT_NOT_FOUND.toString());
		return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}

	/**
	 * A custom general exception
	 */
	@ExceptionHandler({ Exception.class })
	public ResponseEntity<Object> handleExceptions(Exception ex, WebRequest request) {
		log.error("Exception", ex);
		ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage(),
				Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()), request.getDescription(false),
				HttpStatus.INTERNAL_SERVER_ERROR.name());
		return handleExceptionInternal(ex, exceptionResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR,
				request);
	}

	/**
	 * Custom not found exception of Spring
	 */
	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage(),
				ExceptionCode.ERROR_ELEMENT_NOT_FOUND.getStatusCode(), request.getDescription(false),
				ExceptionCode.ERROR_ELEMENT_NOT_FOUND.toString());
		return handleExceptionInternal(ex, exceptionResponse, headers, status, request);
	}

	/**
	 * Custom HttpMessageNotReadableException
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		log.error("Exception", ex);
		ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage(),
				Integer.toString(HttpStatus.BAD_REQUEST.value()), request.getDescription(false),
				ExceptionCode.ERROR_SYNTAX_BAD_REQUEST.toString());
		return handleExceptionInternal(ex, exceptionResponse, headers, status, request);

	}

	/**
	 * Custom Argument Not Valid request
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<String> details = new ArrayList<>();
		ex.getBindingResult().getAllErrors().forEach(error -> {
			log.debug(error.getDefaultMessage());
			details.add(error.getDefaultMessage());

		});
		ExceptionResponse exceptionResponse = new ExceptionResponse(details.get(0),
				Integer.toString(HttpStatus.BAD_REQUEST.value()), request.getDescription(false),
				ExceptionCode.ERROR_SYNTAX_BAD_REQUEST.toString());
		return handleExceptionInternal(ex, exceptionResponse, headers, status, request);
	}

}
