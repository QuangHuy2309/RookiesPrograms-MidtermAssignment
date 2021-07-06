package com.nashtech.MyBikeShop.exception;

public class ObjectAlreadyExistException extends RuntimeException {
	private static final long serialVersionUID = -7806029002430564887L;
	private String message;
	
	public ObjectAlreadyExistException() {
		super();
	}
	
	public ObjectAlreadyExistException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}

