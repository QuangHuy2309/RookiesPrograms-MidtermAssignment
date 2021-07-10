package com.nashtech.MyBikeShop.services;

import java.util.List;
import java.util.Optional;

import com.nashtech.MyBikeShop.DTO.PersonDTO;
import com.nashtech.MyBikeShop.entity.OrderEntity;
import com.nashtech.MyBikeShop.entity.PersonEntity;
import com.nashtech.MyBikeShop.exception.ObjectAlreadyExistException;

public interface PersonService {
	public List<PersonEntity> retrievePersons();
	public Optional<PersonEntity> getPerson(int id);
	public PersonEntity getPerson(String email);
	public PersonEntity createPerson(PersonDTO person);
	public boolean deletePerson(int id);
	public boolean updatePerson(PersonDTO person);
	
}
