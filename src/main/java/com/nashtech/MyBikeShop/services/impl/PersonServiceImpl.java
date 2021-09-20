package com.nashtech.MyBikeShop.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nashtech.MyBikeShop.DTO.PersonDTO;
import com.nashtech.MyBikeShop.entity.PersonEntity;
import com.nashtech.MyBikeShop.exception.ObjectAlreadyExistException;
import com.nashtech.MyBikeShop.exception.ObjectPropertiesIllegalException;
import com.nashtech.MyBikeShop.repository.PersonRepository;
import com.nashtech.MyBikeShop.services.PersonService;

@Service
public class PersonServiceImpl implements PersonService {
	@Autowired
	private PersonRepository personRepository;

	final private PasswordEncoder encoder;

	public PersonServiceImpl(PersonRepository personRepository, PasswordEncoder encoder) {
		this.personRepository = personRepository;
		this.encoder = encoder;
	}

	public List<PersonEntity> retrievePersons() {
		return personRepository.findAll();
	}

	public Optional<PersonEntity> getPerson(int id) {
		return personRepository.findById(id);
	}

	public PersonEntity getPerson(String email) {
		return personRepository.findByEmail(email);
	}

	public List<PersonEntity> getPersonsPage(int num, int size, String role) {
		Sort sortable = Sort.by("id").descending();
		Pageable pageable = PageRequest.of(num, size, sortable);
		return personRepository.findByRole(pageable, role.toUpperCase());
	}

	public PersonEntity createPerson(PersonDTO personDTO) {
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
		PersonEntity personEntity = getPerson(personDTO.getId()).get();
		boolean checkEmailChange = personEntity.getEmail().equals(personDTO.getEmail());
		PersonEntity person = new PersonEntity(personDTO);

		person.setPassword(personEntity.getPassword());
		person.setRole(personEntity.getRole());

		if (checkEmailChange) { // Không đổi email
			personRepository.save(person);
		} else { // Email được đổi, kiểm tra trùng
			List<PersonEntity> list = personRepository.findByEmailIgnoreCase(personDTO.getEmail());
			if (list.isEmpty()) {
				personRepository.save(person);
			} else {
				throw new ObjectAlreadyExistException("There is a user using this email");
			}
		}
		return true;
	}

	public boolean checkExistEmailUpdate(String email, int id) {
		List<PersonEntity> list = personRepository.findByEmailIgnoreCase(email);
		if (list.isEmpty()) {
			return true;
		} else if ((list.size() > 1) || ((list.size() == 1) && (list.get(0).getId() != id)))
			return false;
		else
			return true;
	}

	public int getTotalByRole(String role) {
		return personRepository.countByRole(role);
	}
	
	public PersonEntity changePassword(String email, String oldPassword, String newPassword) {
		PersonEntity person = getPerson(email);
		String newPasswordEncoded = encoder.encode(newPassword);
		boolean isMatched = encoder.matches(oldPassword, person.getPassword());
        if (!isMatched) {
        	throw new ObjectPropertiesIllegalException("Error: Old password is not correct.");
        }
        person.setPassword(newPasswordEncoded);
        return personRepository.save(person);
	}
}
