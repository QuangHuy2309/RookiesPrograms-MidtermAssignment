package com.nashtech.MyBikeShop.exception;

public class ObjectAlreadyExistException extends RuntimeException {
	private static final long serialVersionUID = -7806029002430564887L;
	
	public ObjectAlreadyExistException(String message) {
		super(message);
		
	}
	
}

