package com.nashtech.MyBikeShop.exception;

public class ObjectContainNullException extends RuntimeException{
	private static final long serialVersionUID = 3286152037709951452L;
private String message;
	
	public ObjectContainNullException() {
		super();
	}
	
	public ObjectContainNullException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
