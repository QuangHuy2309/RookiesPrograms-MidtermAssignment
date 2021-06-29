package com.nashtech.MyBikeShop.exception;

public class PersonNotFoundException extends RuntimeException {
	public PersonNotFoundException(String email) {super("Could not find person with email " + email);}
}
