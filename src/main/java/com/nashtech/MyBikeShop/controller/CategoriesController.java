package com.nashtech.MyBikeShop.controller;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nashtech.MyBikeShop.DTO.CategoriesDTO;
import com.nashtech.MyBikeShop.Utils.StringUtils;
import com.nashtech.MyBikeShop.entity.CategoriesEntity;
import com.nashtech.MyBikeShop.exception.ObjectAlreadyExistException;
import com.nashtech.MyBikeShop.exception.ObjectContainNullException;
import com.nashtech.MyBikeShop.exception.ObjectNotFoundException;
import com.nashtech.MyBikeShop.exception.ObjectViolateForeignKeyException;
import com.nashtech.MyBikeShop.payload.request.LoginRequest;
import com.nashtech.MyBikeShop.services.CategoriesService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1")
public class CategoriesController {
	@Autowired
	CategoriesService cateService;

	@Operation(summary = "Get Categories by ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "The request has succeeded", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = CategoriesEntity.class)) }),
			@ApiResponse(responseCode = "401", description = "Unauthorized, Need to login first!", content = @Content),
			@ApiResponse(responseCode = "400", description = "Bad Request: Invalid syntax", content = @Content),
			@ApiResponse(responseCode = "404", description = "Can not find the requested resource", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
	@GetMapping("/categories/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public CategoriesEntity findCategories(@PathVariable(name = "id") int id) {
		return cateService.getCategories(id)
				.orElseThrow(() -> new ObjectNotFoundException("Could not find categories with Id: " + id));
	}
	
	@GetMapping("/categories/checkName")
	@PreAuthorize("hasRole('ADMIN')")
	public boolean findCategories(@RequestParam(name = "name") String name, 
			@RequestParam(name = "id") int id) {
		return cateService.checkExistName(id,name);
	}
	
	@Operation(summary = "Create/Update Categories")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "The request has succeeded", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = CategoriesEntity.class)) }),
			@ApiResponse(responseCode = "401", description = "Unauthorized, Need to login first!", content = @Content),
			@ApiResponse(responseCode = "400", description = "Bad Request: Invalid syntax", content = @Content),
			@ApiResponse(responseCode = "404", description = "Can not find the requested resource", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
	@PostMapping("/categories")
	@PreAuthorize("hasRole('ADMIN')")
	public String createCategories(@RequestBody CategoriesDTO newOrder) {
		try {
			String result = cateService.createCategories(newOrder) ? StringUtils.TRUE : StringUtils.FALSE;
			return result;

		} catch (IllegalArgumentException | ConstraintViolationException | ObjectAlreadyExistException ex) {
			return StringUtils.FALSE;
		}
	}
	
	@PutMapping("/categories/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public String updateCategories(@RequestBody CategoriesDTO newOrder, @PathVariable(name = "id") int id) {
		try {
			String result = cateService.updateCategories(newOrder) ? StringUtils.TRUE : StringUtils.FALSE;
			return result;

		} catch (IllegalArgumentException | ConstraintViolationException | ObjectAlreadyExistException ex) {
			return StringUtils.FALSE;
		}
	}
	
	@Operation(summary = "Delete Categories")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "The request has succeeded", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = CategoriesEntity.class)) }),
			@ApiResponse(responseCode = "401", description = "Unauthorized, Need to login first!", content = @Content),
			@ApiResponse(responseCode = "400", description = "Bad Request: Invalid syntax", content = @Content),
			@ApiResponse(responseCode = "404", description = "Can not find the requested resource", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
	@DeleteMapping("/categories/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public String deleteCategories(@PathVariable(name = "id") int id) {
		try {
			String result = cateService.deleteCategories(id) ? StringUtils.TRUE : StringUtils.FALSE;
			return result;
		} catch (DataIntegrityViolationException ex) {
			throw new ObjectViolateForeignKeyException("This category had at least a product. Delete Product first!");
		}
	}
}
