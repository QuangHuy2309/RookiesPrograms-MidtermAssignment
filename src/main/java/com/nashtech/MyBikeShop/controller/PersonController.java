package com.nashtech.MyBikeShop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nashtech.MyBikeShop.DTO.PersonDTO;
import com.nashtech.MyBikeShop.entity.PersonEntity;
import com.nashtech.MyBikeShop.service.PersonService;

@RestController
@RequestMapping("/api/persons")
public class PersonController {
	@Autowired
	private PersonService personService;

	@GetMapping
	public List<PersonEntity> retrivePerson() {
		return  personService.retrievePersons();
	}

	@GetMapping("/{email}")
	public PersonEntity findPerson(@PathVariable(name = "email") String email) {
		return personService.getPerson(email);
	}

	@PostMapping
	public PersonEntity savePerson(@RequestBody PersonDTO newPerson) {
		return personService.savePerson(newPerson);
	}

	@DeleteMapping("/{email}")
	public void deletePerson(@PathVariable(name = "email") String email) {
		personService.deletePerson(email);
	}

	@PutMapping()
	public void editPerson(@RequestBody PersonDTO newPerson) {
		personService.updatePerson(newPerson);
	}
}
