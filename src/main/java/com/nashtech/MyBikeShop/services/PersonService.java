package com.nashtech.MyBikeShop.services;

import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;

import com.nashtech.MyBikeShop.DTO.PersonDTO;
import com.nashtech.MyBikeShop.entity.PersonEntity;

public interface PersonService {
	public List<PersonEntity> retrievePersons();

	public List<PersonEntity> getPersonsPage(int num, int size, String role);
	
	public List<PersonEntity> searchPerson(String keyword, String role);
	
	public List<PersonEntity> searchPersonRoleNot(String keyword, String role);

	public Optional<PersonEntity> getPerson(int id);

	public PersonEntity getPerson(String email);

	public PersonEntity createPerson(PersonDTO person);

	public boolean deletePerson(PersonEntity person);

	public boolean updatePerson(PersonDTO person);

	public int getTotalByRole(String role);

	public boolean checkExistEmailUpdate(String email, int id);
	
	public PersonEntity changePassword(String email, String oldPassword, String newPassword);
	
	public PersonEntity forgotPassword(String email, String newPassword);
	
	public void sendOTPEmail(String email) throws MessagingException;
	
	public String createOTP();
	
	public boolean checkOTP(String email, String otp);
}
