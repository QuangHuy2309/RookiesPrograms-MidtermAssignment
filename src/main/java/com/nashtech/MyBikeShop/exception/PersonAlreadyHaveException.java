package com.nashtech.MyBikeShop.exception;

public class PersonAlreadyHaveException extends RuntimeException {
	public PersonAlreadyHaveException(String email) {super("There is a person with email " + email);}
}

