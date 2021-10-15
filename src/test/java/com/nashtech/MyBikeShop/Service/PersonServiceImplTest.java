package com.nashtech.MyBikeShop.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.nashtech.MyBikeShop.DTO.PersonDTO;
import com.nashtech.MyBikeShop.entity.PersonEntity;
import com.nashtech.MyBikeShop.repository.PersonRepository;
import com.nashtech.MyBikeShop.services.PersonService;
import com.nashtech.MyBikeShop.services.impl.PersonServiceImpl;

@SpringBootTest
public class PersonServiceImplTest {
	
	@Mock
	PasswordEncoder encode;
	
	@Mock
	private PersonRepository personRepository;
	
	@InjectMocks
	PersonService personService = new PersonServiceImpl(personRepository, encode);
	
	private PersonEntity person1;
	private PersonEntity person2;
	private PersonEntity person3;
	private PersonDTO personDTO;
	List<PersonEntity> listPerson;
	private final int list_size = 3;
	
	@BeforeEach
	public void setup() {
		person1 = new PersonEntity(1,"test1@gmail.com","123456","Test 1","ADMIN");
		person2 = new PersonEntity(2,"test2@gmail.com","123456","Test 2","STAFF");
		person3 = new PersonEntity(3,"test3@gmail.com","123456","Test 3","USER");
		personDTO = new PersonDTO(3,"test3@gmail.com","123456","Test 3","USER");
		listPerson = new ArrayList<PersonEntity>(List.of(person1,person2,person3));
	}
	
	@Test
	public void retrievePersonsSuccess_Test() {
		when(personRepository.findAll()).thenReturn(listPerson);
		List<PersonEntity> listPerson_test = personService.retrievePersons();
		assertEquals(list_size, listPerson_test.size());
	}
	
	@Test
	public void createPersonSuccess_Test() {
		when(personRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(null));
		when(personRepository.save(Mockito.any(PersonEntity.class))).thenReturn(person1);
		PersonEntity person_test = personService.createPerson(personDTO);
		assertEquals(person1, person_test);
	}
}
