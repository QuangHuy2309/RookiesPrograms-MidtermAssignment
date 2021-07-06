package com.nashtech.MyBikeShop.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

import com.nashtech.MyBikeShop.exception.ObjectAlreadyExistException;
import com.nashtech.MyBikeShop.exception.ObjectNotFoundException;
import com.nashtech.MyBikeShop.exception.ObjectViolateForeignKeyException;
import com.nashtech.MyBikeShop.payload.response.MessageResponse;

@ControllerAdvice
public class SecurityControllerAdvice {
	@ExceptionHandler(ObjectAlreadyExistException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public MessageResponse handleSecurityException(ObjectAlreadyExistException ex) {
		return new MessageResponse(ex.getMessage());
	}

	@ExceptionHandler(ObjectViolateForeignKeyException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public MessageResponse handleSecurityException(ObjectViolateForeignKeyException ex) {
		return new MessageResponse(ex.getMessage());
	}

	@ExceptionHandler(ObjectNotFoundException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public MessageResponse handleSecurityException(ObjectNotFoundException ex) {
		return new MessageResponse(ex.getMessage());
	}

}
