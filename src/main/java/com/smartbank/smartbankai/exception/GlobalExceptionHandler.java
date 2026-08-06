package com.smartbank.smartbankai.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public Map<String, String> handleValidationException(
	        MethodArgumentNotValidException ex) {

	    Map<String, String> errors = new HashMap<>();

	    ex.getBindingResult().getFieldErrors().forEach(error -> {
	        errors.put(error.getField(), error.getDefaultMessage());
	    });

	    return errors;
	}
	@ExceptionHandler(EmailAlreadyExistsException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public Map<String, String> handleEmailAlreadyExistsException(
	        EmailAlreadyExistsException ex) {

	    Map<String, String> error = new HashMap<>();
	    error.put("error", ex.getMessage());

	    return error;
	}
	@ExceptionHandler(UserNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public Map<String, String> handleUserNotFoundException(
	        UserNotFoundException ex) {

	    Map<String, String> error = new HashMap<>();
	    error.put("error", ex.getMessage());

	    return error;
	}
	@ExceptionHandler(AccountNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public Map<String, String> handleAccountNotFoundException(
	        AccountNotFoundException ex) {

	    Map<String, String> error = new HashMap<>();
	    error.put("error", ex.getMessage());

	    return error;
	}
	@ExceptionHandler(InsufficientBalanceException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public Map<String, String> handleInsufficientBalanceException(
	        InsufficientBalanceException ex) {

	    Map<String, String> error = new HashMap<>();
	    error.put("error", ex.getMessage());

	    return error;
	}
	@ExceptionHandler(UnauthorizedAccessException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ResponseBody
	public Map<String, String> handleUnauthorizedAccessException(
	        UnauthorizedAccessException ex) {

	    Map<String, String> error = new HashMap<>();
	    error.put("error", ex.getMessage());

	    return error;
	}
	@ExceptionHandler(InvalidCredentialsException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ResponseBody
	public Map<String, String> handleInvalidCredentialsException(
	        InvalidCredentialsException ex) {

	    Map<String, String> error = new HashMap<>();
	    error.put("error", ex.getMessage());

	    return error;
	}
	@ExceptionHandler(RuntimeException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public Map<String, String> handleRuntimeException(RuntimeException ex) {

	    Map<String, String> error = new HashMap<>();
	    error.put("error", ex.getMessage());

	    return error;
	}
}