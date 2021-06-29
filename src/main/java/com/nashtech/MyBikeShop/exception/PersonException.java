package com.nashtech.MyBikeShop.exception;

public class PersonException extends RuntimeException {
	public PersonException(String email) {
		super("Could not find person with email " + email);
	}
}
