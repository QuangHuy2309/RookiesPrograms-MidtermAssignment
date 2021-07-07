package com.nashtech.MyBikeShop.exception;

public class ObjectPropertiesNullException extends IllegalArgumentException {
	private static final long serialVersionUID = -7806029002430564887L;
	private String message;
	
	public ObjectPropertiesNullException() {
		super();
	}
	
	public ObjectPropertiesNullException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}

