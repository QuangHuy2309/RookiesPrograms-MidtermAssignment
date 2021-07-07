package com.nashtech.MyBikeShop.exception;

public class WrongInputTypeException extends RuntimeException {
static final long serialVersionUID = 1458188705399990162L;
	private String message;
	
	public WrongInputTypeException() {
		super();
	}
	
	public WrongInputTypeException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}

