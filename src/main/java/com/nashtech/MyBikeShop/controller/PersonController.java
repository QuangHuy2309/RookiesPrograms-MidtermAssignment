package com.nashtech.MyBikeShop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nashtech.MyBikeShop.DTO.PersonDTO;
import com.nashtech.MyBikeShop.entity.OrderEntity;
import com.nashtech.MyBikeShop.entity.PersonEntity;
import com.nashtech.MyBikeShop.services.PersonService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/persons")
public class PersonController {
	@Autowired
	private PersonService personService;

	@Operation(summary = "Get all Account Infomation")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "The request has succeeded", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = PersonEntity.class)) }),
			@ApiResponse(responseCode = "401", description = "Unauthorized, Need to login first!", content = @Content),
			@ApiResponse(responseCode = "400", description = "Bad Request: Invalid syntax", content = @Content),
			@ApiResponse(responseCode = "404", description = "Can not find the requested resource", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public List<PersonEntity> retrivePerson() {
		return personService.retrievePersons();
	}

	@Operation(summary = "Get Account Infomation by Email")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "The request has succeeded", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = PersonEntity.class)) }),
			@ApiResponse(responseCode = "401", description = "Unauthorized, Need to login first!", content = @Content),
			@ApiResponse(responseCode = "400", description = "Bad Request: Invalid syntax", content = @Content),
			@ApiResponse(responseCode = "404", description = "Can not find the requested resource", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
	@GetMapping("/{email}")
	@PreAuthorize("hasRole('ADMIN')")
	public PersonEntity findPerson(@PathVariable(name = "email") String email) {
		return personService.getPerson(email);
	}

//	@Operation(summary = "Get create Account Infomation")
//	@ApiResponses(value = {
//			@ApiResponse(responseCode = "200", description = "The request has succeeded", 
//			content = { @Content(mediaType = "application/json", 
//			schema = @Schema(implementation = PersonEntity.class))}),
//			@ApiResponse(responseCode = "401", description = "Unauthorized, Need to login first!", 
//			content = @Content),
//			@ApiResponse(responseCode = "400", description = "Bad Request: Invalid syntax", 
//			content = @Content),
//			@ApiResponse(responseCode = "404", description = "Can not find the requested resource", 
//			content = @Content),
//			@ApiResponse(responseCode = "500", description = "Internal Server Error", 
//			content = @Content)
//	})
//	@PostMapping
//	public PersonEntity createPerson(@RequestBody PersonDTO newPerson) {
//		return personService.createPerson(newPerson);
//	}
	@Operation(summary = "Delete Account by Email")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "The request has succeeded", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = PersonEntity.class)) }),
			@ApiResponse(responseCode = "401", description = "Unauthorized, Need to login first!", content = @Content),
			@ApiResponse(responseCode = "400", description = "Bad Request: Invalid syntax", content = @Content),
			@ApiResponse(responseCode = "404", description = "Can not find the requested resource", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
	@DeleteMapping("/{email}")
	@PreAuthorize("hasRole('ADMIN')")
	public String deletePerson(@PathVariable(name = "email") String email) {
		return personService.deletePerson(email);
	}

	@Operation(summary = "Update Account Infomation")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "The request has succeeded", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = PersonEntity.class)) }),
			@ApiResponse(responseCode = "401", description = "Unauthorized, Need to login first!", content = @Content),
			@ApiResponse(responseCode = "400", description = "Bad Request: Invalid syntax", content = @Content),
			@ApiResponse(responseCode = "404", description = "Can not find the requested resource", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
	@PutMapping
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public void editPerson(@RequestBody PersonDTO newPerson) {
		personService.updatePerson(newPerson);
	}

}
