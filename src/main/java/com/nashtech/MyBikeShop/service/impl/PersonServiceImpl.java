package com.nashtech.MyBikeShop.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nashtech.MyBikeShop.entity.Person;
import com.nashtech.MyBikeShop.repository.PersonRepository;
import com.nashtech.MyBikeShop.service.PersonService;

@Service
public class PersonServiceImpl implements PersonService{
	@Autowired
	private PersonRepository personRepository;
	
	public void setPersonRepository(PersonRepository personRepository) {
		this.personRepository = personRepository;
	}
	public List<Person> retrievePersons(){
		List<Person> persons = personRepository.findAll();
		return persons;
	}
	
	public Person getPerson(String email) {
		Optional<Person> optperson = personRepository.findById(email);
		Person person = optperson.get();
		return person;
	}
	public Person savePerson(Person person) {return personRepository.save(person);}
	public void deletePerson(String email) {personRepository.deleteById(email);}
	public void updatePerson(Person person) {personRepository.save(person);}
}
