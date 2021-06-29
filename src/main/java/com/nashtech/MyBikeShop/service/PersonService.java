package com.nashtech.MyBikeShop.service;

import java.util.List;

import com.nashtech.MyBikeShop.entity.Person;

public interface PersonService {
	public List<Person> retrievePersons();
	public Person getPerson(String email);
	public Person savePerson(Person person);
	public void deletePerson(String email);
	public void updatePerson(Person person);
}
