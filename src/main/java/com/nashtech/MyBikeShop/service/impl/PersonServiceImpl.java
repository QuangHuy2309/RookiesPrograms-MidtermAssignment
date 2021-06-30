package com.nashtech.MyBikeShop.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nashtech.MyBikeShop.DTO.PersonDTO;
import com.nashtech.MyBikeShop.entity.PersonEntity;
import com.nashtech.MyBikeShop.exception.PersonAlreadyHaveException;
import com.nashtech.MyBikeShop.exception.PersonNotFoundException;
import com.nashtech.MyBikeShop.repository.PersonRepository;
import com.nashtech.MyBikeShop.service.PersonService;

@Service
public class PersonServiceImpl implements PersonService {
	@Autowired
	private PersonRepository personRepository;

	public void setPersonRepository(PersonRepository personRepository) {
		this.personRepository = personRepository;
	}

	public List<PersonEntity> retrievePersons() {
		return personRepository.findAll();
	}

	public PersonEntity getPerson(String email) {
//		Optional<Person> optperson = personRepository.findById(email);
//		Person person = optperson.get();
//		return person;
		return personRepository.findById(email).orElseThrow(() -> new PersonNotFoundException(email));
	}

	public PersonEntity createPerson(PersonDTO personDTO) {
		Optional<PersonEntity> person = personRepository.findById(personDTO.getEmail());
		if (person.isPresent()) {
			throw new PersonAlreadyHaveException(person.get().getEmail());
		} else {
			PersonEntity personEntity = new PersonEntity(personDTO);
			return personRepository.save(personEntity);
		}
	}

	public void deletePerson(String email) {
		personRepository.deleteById(email);
	}

	public void updatePerson(PersonDTO personDTO) {
		PersonEntity person = new PersonEntity(personDTO);
		personRepository.save(person);
	}
}