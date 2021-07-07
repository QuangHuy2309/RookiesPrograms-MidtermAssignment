package com.nashtech.MyBikeShop.exception;

import org.springframework.dao.EmptyResultDataAccessException;

public class ObjectNotFoundException extends EmptyResultDataAccessException {
	private static final long serialVersionUID = 7524919075763991340L;
	private String message;

	public ObjectNotFoundException() {
		super(1);
	}

	public ObjectNotFoundException(String message) {
		super(1);
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
