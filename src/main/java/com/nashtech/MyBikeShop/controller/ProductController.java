package com.nashtech.MyBikeShop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.multipart.MultipartFile;

import com.nashtech.MyBikeShop.DTO.ProductDTO;
import com.nashtech.MyBikeShop.Utils.StringUtils;
import com.nashtech.MyBikeShop.entity.PersonEntity;
import com.nashtech.MyBikeShop.entity.ProductEntity;
import com.nashtech.MyBikeShop.exception.ObjectAlreadyExistException;
import com.nashtech.MyBikeShop.exception.ObjectNotFoundException;
import com.nashtech.MyBikeShop.services.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class ProductController {
	@Autowired
	private ProductService productService;

	@Operation(summary = "Create a Product")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "The request has succeeded", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ProductEntity.class)) }),
			@ApiResponse(responseCode = "303", description = "Error: Have an existed product ", content = @Content),
			@ApiResponse(responseCode = "401", description = "Unauthorized, Need to login first!", content = @Content),
			@ApiResponse(responseCode = "400", description = "Bad Request: Invalid syntax", content = @Content),
			@ApiResponse(responseCode = "403", description = "Forbidden: Only ADMIN can create Product", content = @Content),
			@ApiResponse(responseCode = "404", description = "Can not find the requested resource", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
	@PostMapping("/product")
	@PreAuthorize("hasRole('ADMIN')")
	public ProductEntity saveProduct(@RequestBody ProductDTO newProduct) {
				return productService.createProduct(newProduct);
	}
	
	@GetMapping("/product/checkExistId/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public boolean checkExistId(@PathVariable(name = "id") String id) {
				return productService.existId(id);
	}
	@GetMapping("/product/checkExistName/{name}")
	@PreAuthorize("hasRole('ADMIN')")
	public boolean checkExistName(@PathVariable(name = "name") String name) {
				return productService.existName(name);
	}
	@GetMapping("/product/checkExistNameUpdate")
	@PreAuthorize("hasRole('ADMIN')")
	public boolean checkExistName(@RequestParam(name = "name") String name, 
			@RequestParam(name = "id") String id) {
				return productService.existNameUpdate(id, name);
	}
	@Operation(summary = "Delete a Product by id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "The request has succeeded", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ProductEntity.class)) }),
			@ApiResponse(responseCode = "401", description = "Unauthorized, Need to login first!", content = @Content),
			@ApiResponse(responseCode = "400", description = "Bad Request: Invalid syntax", content = @Content),
			@ApiResponse(responseCode = "404", description = "Can not find the requested resource", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
	@DeleteMapping("/product/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public String deleteProduct(@PathVariable(name = "id") String id) {
		try {
		return productService.deleteProduct(id) ? StringUtils.TRUE : StringUtils.FALSE;
		}catch (DataIntegrityViolationException | EmptyResultDataAccessException ex) {
			return StringUtils.FALSE;
		}
	}

	@Operation(summary = "Update a Product")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "The request has succeeded", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ProductEntity.class)) }),
			@ApiResponse(responseCode = "401", description = "Unauthorized, Need to login first!", content = @Content),
			@ApiResponse(responseCode = "400", description = "Bad Request: Invalid syntax", content = @Content),
			@ApiResponse(responseCode = "404", description = "Can not find the requested resource", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
	@PutMapping("/product/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public String editProduct(@RequestBody ProductDTO product, @PathVariable(name = "id") String id) {
		return productService.updateProduct(product) ? StringUtils.TRUE : StringUtils.FALSE;
	}
	
//	@PostMapping("/img")
//	@PreAuthorize("hasRole('ADMIN')")
//	public String saveImgProduct(@RequestParam("file") MultipartFile file, @RequestParam("id") String id) {
//		try {
//			return productService.storeImage(file, id) ? StringUtils.TRUE : StringUtils.FALSE;
//			
//		}catch(Exception e) {
//			return StringUtils.FALSE;
//		}
//	}
	
	
}
