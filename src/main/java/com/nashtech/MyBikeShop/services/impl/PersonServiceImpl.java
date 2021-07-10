package com.nashtech.MyBikeShop.services.impl;

import java.util.stream.Collectors;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nashtech.MyBikeShop.DTO.PersonDTO;
import com.nashtech.MyBikeShop.entity.OrderEntity;
import com.nashtech.MyBikeShop.entity.PersonEntity;
import com.nashtech.MyBikeShop.entity.UserEntity;
import com.nashtech.MyBikeShop.exception.ObjectAlreadyExistException;
import com.nashtech.MyBikeShop.exception.ObjectNotFoundException;
import com.nashtech.MyBikeShop.repository.PersonRepository;
import com.nashtech.MyBikeShop.services.PersonService;

@Service
public class PersonServiceImpl implements PersonService {
	@Autowired
	private PersonRepository personRepository;

	public PersonServiceImpl(PersonRepository personRepository) {
		this.personRepository = personRepository;
	}

	public List<PersonEntity> retrievePersons() {
		return personRepository.findAll();
	}

	public Optional<PersonEntity> getPerson(int id) {
//		Optional<Person> optperson = personRepository.findById(email);
//		Person person = optperson.get();
//		return person;
		return personRepository.findById(id);
	}
	
	public PersonEntity getPerson(String email) {
//		Optional<Person> optperson = personRepository.findById(email);
//		Person person = optperson.get();
//		return person;
		return personRepository.findByEmail(email);
	}

	public PersonEntity createPerson(PersonDTO personDTO){
		Optional<PersonEntity> person = personRepository.findById(personDTO.getId());
		if (person.isPresent()) {
			throw new ObjectAlreadyExistException("There is a person with email " + person.get().getEmail());
		} else {
			PersonEntity personEntity = new PersonEntity(personDTO);
			return personRepository.save(personEntity);
		}
	}

	public boolean deletePerson(int id) {
		personRepository.deleteById(id);
		return true;
	}

	public boolean updatePerson(PersonDTO personDTO) {
		boolean checkEmailChange = getPerson(personDTO.getId()).get().getEmail().equals(personDTO.getEmail());
		if (checkEmailChange) {		// Không đổi email
		personRepository.save(new PersonEntity(personDTO));
		}
		else { // Email được đổi, kiểm tra trùng
			PersonEntity personCheck = personRepository.findByEmail(personDTO.getEmail());
			if (personCheck == null) {
				personRepository.save(new PersonEntity(personDTO));
			}
			else {
				throw new ObjectAlreadyExistException("There is a user using this email");
			}
		}
		return true;
	}

}
