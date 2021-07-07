package com.nashtech.MyBikeShop.exception;

public class ObjectPropertiesIllegalException extends IllegalArgumentException {
	private static final long serialVersionUID = -7806029002430564887L;
	private String message;
	
	public ObjectPropertiesIllegalException() {
		super();
	}
	
	public ObjectPropertiesIllegalException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}

