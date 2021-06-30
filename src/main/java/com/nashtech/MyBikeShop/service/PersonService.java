package com.nashtech.MyBikeShop.service;

import java.util.List;

import com.nashtech.MyBikeShop.DTO.PersonDTO;
import com.nashtech.MyBikeShop.entity.PersonEntity;

public interface PersonService {
	public List<PersonEntity> retrievePersons();
	public PersonEntity getPerson(String email);
	public PersonEntity savePerson(PersonDTO person);
	public void deletePerson(String email);
	public void updatePerson(PersonDTO person);
}
