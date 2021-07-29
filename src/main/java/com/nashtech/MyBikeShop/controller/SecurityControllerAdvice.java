package com.nashtech.MyBikeShop.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;

import com.nashtech.MyBikeShop.exception.*;
import com.nashtech.MyBikeShop.payload.response.MessageResponse;

@ControllerAdvice
public class SecurityControllerAdvice extends ResponseEntityExceptionHandler {
	@ExceptionHandler(ObjectAlreadyExistException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public void handleExistException(HttpServletResponse response, ObjectAlreadyExistException ex) throws IOException {
		System.err.println(ex.getMessage());
		response.sendError(409);
	}

	@ExceptionHandler(ObjectViolateForeignKeyException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public void handleViolateForeignKeyException(HttpServletResponse response, ObjectViolateForeignKeyException ex)
			throws IOException {
		response.sendError(409);
		System.err.println(ex.getMessage());

	}

	@ExceptionHandler(ObjectNotFoundException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public void handleNotFoundException(HttpServletResponse response, ObjectNotFoundException ex) throws IOException {
		response.sendError(404);
		System.err.println(ex.getMessage());

	}

	@ExceptionHandler(ObjectPropertiesIllegalException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public void handleIllegealException(HttpServletResponse response, ObjectPropertiesIllegalException ex)
			throws IOException {
		response.sendError(400);
		System.err.println(ex.getMessage());
	}

	@ExceptionHandler(JsonGetDataException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public MessageResponse handleSecurityException(HttpServletResponse response, JsonGetDataException ex) {
		System.err.println(ex.getMessage());
		return new MessageResponse(ex.getMessage());
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public MessageResponse handleTypeMismatch(HttpServletResponse response, MethodArgumentTypeMismatchException ex) {
		String name = ex.getName();
		String type = ex.getRequiredType().getSimpleName();
		Object value = ex.getValue();
		String message = String.format("ERROR: '%s' should be a valid '%s' and '%s' isn't", name, type, value);

		System.err.println(message);
		return new MessageResponse(message);
	}

	@ExceptionHandler(ObjectContainNullException.class)
	public MessageResponse handleTypeMismatch(HttpServletResponse response, ObjectContainNullException ex) {
		System.err.println(ex.getMessage());
		return new MessageResponse(ex.getMessage());
	}
}
