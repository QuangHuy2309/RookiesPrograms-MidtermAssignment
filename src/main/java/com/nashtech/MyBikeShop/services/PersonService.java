package com.nashtech.MyBikeShop.services;

import java.util.List;
import java.util.Optional;

import com.nashtech.MyBikeShop.DTO.PersonDTO;
import com.nashtech.MyBikeShop.entity.PersonEntity;

public interface PersonService {
	public List<PersonEntity> retrievePersons();

	public List<PersonEntity> getPersonsPage(int num, int size, String role);

	public Optional<PersonEntity> getPerson(int id);

	public PersonEntity getPerson(String email);

	public PersonEntity createPerson(PersonDTO person);

	public boolean deletePerson(int id);

	public boolean updatePerson(PersonDTO person);

	public int getTotalByRole(String role);

	public boolean checkExistEmailUpdate(String email, int id);
}
