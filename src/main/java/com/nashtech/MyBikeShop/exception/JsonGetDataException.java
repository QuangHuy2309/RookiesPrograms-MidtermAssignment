package com.nashtech.MyBikeShop.exception;

public class JsonGetDataException extends NullPointerException {
	private static final long serialVersionUID = 1947077612237154249L;
	private String message;

	public JsonGetDataException() {
		super();
	}

	public JsonGetDataException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
