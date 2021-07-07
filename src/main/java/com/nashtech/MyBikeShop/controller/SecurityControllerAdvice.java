package com.nashtech.MyBikeShop.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.http.HttpStatus;

import com.nashtech.MyBikeShop.exception.*;
import com.nashtech.MyBikeShop.payload.response.MessageResponse;

@ControllerAdvice
public class SecurityControllerAdvice {
	@ExceptionHandler(ObjectAlreadyExistException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public MessageResponse handleSecurityException(ObjectAlreadyExistException ex) {
		System.err.println(ex.getMessage());
		return new MessageResponse(ex.getMessage());
	}

	@ExceptionHandler(ObjectViolateForeignKeyException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public MessageResponse handleSecurityException(ObjectViolateForeignKeyException ex) {
		System.err.println(ex.getMessage());
		return new MessageResponse(ex.getMessage());
	}

	@ExceptionHandler(ObjectNotFoundException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public MessageResponse handleSecurityException(ObjectNotFoundException ex) {
		System.err.println(ex.getMessage());
		return new MessageResponse(ex.getMessage());
	}

	@ExceptionHandler(ObjectPropertiesIllegalException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public MessageResponse handleSecurityException(ObjectPropertiesIllegalException ex) {
		System.err.println(ex.getMessage());
		return new MessageResponse(ex.getMessage());
	}

	@ExceptionHandler(JsonGetDataException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public MessageResponse handleSecurityException(JsonGetDataException ex) {
		System.err.println(ex.getMessage());
		return new MessageResponse(ex.getMessage());
	}
	
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public MessageResponse handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
	    String name = ex.getName();
	    String type = ex.getRequiredType().getSimpleName();
	    Object value = ex.getValue();
	    String message = String.format("ERROR: '%s' should be a valid '%s' and '%s' isn't", 
	                                   name, type, value);

	    System.err.println(message);
	    return new MessageResponse(message);
	}
	
	@ExceptionHandler(ObjectContainNullException.class)
	public MessageResponse handleTypeMismatch(ObjectContainNullException ex) {
		System.err.println(ex.getMessage());
	    return new MessageResponse(ex.getMessage());
	}
}
