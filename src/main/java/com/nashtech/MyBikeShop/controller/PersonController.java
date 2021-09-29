package com.nashtech.MyBikeShop.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nashtech.MyBikeShop.DTO.PersonDTO;
import com.nashtech.MyBikeShop.Utils.StringUtils;
import com.nashtech.MyBikeShop.entity.PersonEntity;
import com.nashtech.MyBikeShop.exception.ObjectNotFoundException;
import com.nashtech.MyBikeShop.services.PersonService;
import com.nashtech.MyBikeShop.payload.response.MessageResponse;
import com.nashtech.MyBikeShop.payload.request.ChangePasswordRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class PersonController {
	@Autowired
	private PersonService personService;

	@Operation(summary = "Get all Account Infomation by Role")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "The request has succeeded", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = PersonEntity.class)) }),
			@ApiResponse(responseCode = "401", description = "Unauthorized, Need to login first!", content = @Content),
			@ApiResponse(responseCode = "400", description = "Bad Request: Invalid syntax", content = @Content),
			@ApiResponse(responseCode = "404", description = "Can not find the requested resource", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
	@GetMapping("/persons")
	@PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
	public List<PersonEntity> getPersonbyRole(@RequestParam(name = "pagenum") int page,
			@RequestParam(name = "size") int size, @RequestParam(name = "role") String role) {
		return personService.getPersonsPage(page, size, role);
	}

	@GetMapping("/persons/search")
	@PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
	public List<PersonEntity> searchPersonbyRole(@RequestParam(name = "keyword") String keyword,
			@RequestParam(name = "role") String role) {
		return personService.searchPerson(keyword, role);
	}
	
	@GetMapping("/persons/search/roleNot")
	@PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
	public List<PersonEntity> searchPersonbyRoleNot(@RequestParam(name = "keyword") String keyword,
			@RequestParam(name = "role") String role) {
		return personService.searchPersonRoleNot(keyword, role);
	}

	@Operation(summary = "Get Account Infomation by Id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "The request has succeeded", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = PersonEntity.class)) }),
			@ApiResponse(responseCode = "401", description = "Unauthorized, Need to login first!", content = @Content),
			@ApiResponse(responseCode = "400", description = "Bad Request: Invalid syntax", content = @Content),
			@ApiResponse(responseCode = "404", description = "Can not find the requested resource", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
	@GetMapping("/persons/search/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('STAFF') or hasRole('ADMIN')")
	public PersonEntity findPerson(@PathVariable(name = "id") int id) {
		try {
			return personService.getPerson(id).get();
		} catch (NoSuchElementException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
		}
	}

	@GetMapping("/persons/checkEmailUpdate")
	@PreAuthorize("hasRole('USER') or hasRole('STAFF') or hasRole('ADMIN')")
	public boolean findPerson(@RequestParam(name = "email") String email, @RequestParam(name = "id") int id) {
		return personService.checkExistEmailUpdate(email, id);

	}

	@GetMapping("/persons/search/email/{email}")
	@PreAuthorize("hasRole('USER') or hasRole('STAFF') or hasRole('ADMIN')")
	public PersonEntity findPersonByEmail(@PathVariable(name = "email") String email) {
		try {
			return personService.getPerson(email);
		} catch (NoSuchElementException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
		}
	}

	@Operation(summary = "Delete Account by Email")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "The request has succeeded", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = PersonEntity.class)) }),
			@ApiResponse(responseCode = "401", description = "Unauthorized, Need to login first!", content = @Content),
			@ApiResponse(responseCode = "400", description = "Bad Request: Invalid syntax", content = @Content),
			@ApiResponse(responseCode = "404", description = "Can not find the requested resource", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
	@DeleteMapping("/persons/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public String deletePerson(@PathVariable(name = "id") int id) {
		try {
			return personService.deletePerson(id) ? StringUtils.TRUE : StringUtils.FALSE;
		} catch (DataIntegrityViolationException | EmptyResultDataAccessException ex) {
			return StringUtils.FALSE;
		}
	}

	@Operation(summary = "Update Account Infomation")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "The request has succeeded", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = PersonEntity.class)) }),
			@ApiResponse(responseCode = "401", description = "Unauthorized, Need to login first!", content = @Content),
			@ApiResponse(responseCode = "400", description = "Bad Request: Invalid syntax", content = @Content),
			@ApiResponse(responseCode = "404", description = "Can not find the requested resource", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
	@PutMapping("/persons/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('STAFF') or hasRole('ADMIN')")
	public String editPerson(@RequestBody PersonDTO newPerson, @PathVariable(name = "id") int id) {
		return personService.updatePerson(newPerson) ? StringUtils.TRUE : StringUtils.FALSE;
	}

	@GetMapping("/persons/countByRole/{role}")
	@PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
	public int getTotalByRole(@PathVariable(name = "role") String role) {
		return personService.getTotalByRole(role);
	}

	@PutMapping("/persons/updatePassword/{email}")
	@PreAuthorize("hasRole('USER') or hasRole('STAFF') or hasRole('ADMIN')")
	public ResponseEntity<?> updatePassword(@PathVariable String email,
			@RequestBody ChangePasswordRequest changePasswordRequest) {
		try {
			PersonEntity updateAccount = personService.changePassword(email, changePasswordRequest.getOldPassword(),
					changePasswordRequest.getNewPassword());
			if (updateAccount == null)
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Change password failed."));
			return ResponseEntity.ok().body(new MessageResponse("Update password successfully."));
		} catch (NoSuchElementException ex) {
			throw new ObjectNotFoundException("Error: Not found account with email: " + email);
		}

	}
}
