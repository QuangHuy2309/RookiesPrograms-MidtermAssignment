package com.nashtech.MyBikeShop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nashtech.MyBikeShop.DTO.RateDTO;
import com.nashtech.MyBikeShop.entity.CategoriesEntity;
import com.nashtech.MyBikeShop.entity.ProductEntity;
import com.nashtech.MyBikeShop.entity.RateEntity;
import com.nashtech.MyBikeShop.exception.ObjectNotFoundException;
import com.nashtech.MyBikeShop.services.CategoriesService;
import com.nashtech.MyBikeShop.services.ProductService;
import com.nashtech.MyBikeShop.services.RateService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/public")
public class PublicController {
	@Autowired
	private ProductService productService;
	
	@Autowired
	CategoriesService cateService;
	
	@Autowired
	RateService rateService;

	@Operation(summary = "Get all Categories") // CATEGORIES
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "The request has succeeded", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = CategoriesEntity.class)) }),
			@ApiResponse(responseCode = "401", description = "Unauthorized, Need to login first!", content = @Content),
			@ApiResponse(responseCode = "404", description = "Can not find the requested resource", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
	@GetMapping("/categories")
	public List<CategoriesEntity> retrieveCategories() {
		return cateService.retrieveCategories();
	}
	
	@Operation(summary = "Get all Product Infomation") //PRODUCT
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "The request has succeeded", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ProductEntity.class)) }),
			@ApiResponse(responseCode = "401", description = "Unauthorized, Need to login first!", content = @Content),
			@ApiResponse(responseCode = "400", description = "Bad Request: Invalid syntax", content = @Content),
			@ApiResponse(responseCode = "404", description = "Can not find the requested resource", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
	@GetMapping("/product")
	public List<ProductEntity> retrieveProducts() {
		return productService.retrieveProducts();
	}
	
	@Operation(summary = "Get a Product Infomation by ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "The request has succeeded", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ProductEntity.class)) }),
			@ApiResponse(responseCode = "401", description = "Unauthorized, Need to login first!", content = @Content),
			@ApiResponse(responseCode = "400", description = "Bad Request: Invalid syntax", content = @Content),
			@ApiResponse(responseCode = "404", description = "Can not find the requested resource", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
	@GetMapping("/product/search/{id}")
	public ProductEntity getProduct(@PathVariable(name = "id") String id) {
		return productService.getProduct(id)
				.orElseThrow(() -> new ObjectNotFoundException("Could not find product with Id: " + id));
	}
	
//	@Operation(summary = "Get a list of Product Infomation by Category")
//	@ApiResponses(value = {
//			@ApiResponse(responseCode = "200", description = "The request has succeeded", content = {
//					@Content(mediaType = "application/json", schema = @Schema(implementation = ProductEntity.class)) }),
//			@ApiResponse(responseCode = "401", description = "Unauthorized, Need to login first!", content = @Content),
//			@ApiResponse(responseCode = "400", description = "Bad Request: Invalid syntax", content = @Content),
//			@ApiResponse(responseCode = "404", description = "Can not find the requested resource", content = @Content),
//			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
//	@GetMapping("/product/searchtype/{id}")
//	public List<ProductEntity> getAllProductbyCategory(@PathVariable(name = "id") int id) {
//		return productService.findProductByCategories(id);
//	}
	
	@Operation(summary = "Get Product by Type for Page")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "The request has succeeded", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ProductEntity.class)) }),
			@ApiResponse(responseCode = "401", description = "Unauthorized, Need to login first!", content = @Content),
			@ApiResponse(responseCode = "400", description = "Bad Request: Invalid syntax", content = @Content),
			@ApiResponse(responseCode = "404", description = "Can not find the requested resource", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
	@GetMapping("/product/page")
	public List<ProductEntity> getProductPage(@RequestParam(name = "pagenum") int page, 
				@RequestParam(name = "size") int size,@RequestParam(name = "type") int id) {
		return productService.getProductPage(page, size, id);
	}
	
	@Operation(summary = "Get Top Product by Type for Welcome Page")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "The request has succeeded", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ProductEntity.class)) }),
			@ApiResponse(responseCode = "401", description = "Unauthorized, Need to login first!", content = @Content),
			@ApiResponse(responseCode = "400", description = "Bad Request: Invalid syntax", content = @Content),
			@ApiResponse(responseCode = "404", description = "Can not find the requested resource", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
	@GetMapping("/product/categories/{id}")
	public List<ProductEntity> getNewstProductByCategories(@PathVariable(name = "id") int id) {
		return productService.getNewestProductCategories(id, 4);
	}
	
	
	@Operation(summary = "Get a list of Rate for Product") // RATE OF PRODUCT
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "The request has succeeded", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ProductEntity.class)) }),
			@ApiResponse(responseCode = "401", description = "Unauthorized, Need to login first!", content = @Content),
			@ApiResponse(responseCode = "400", description = "Bad Request: Invalid syntax", content = @Content),
			@ApiResponse(responseCode = "404", description = "Can not find the requested resource", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
	@GetMapping("/product/rate/{id}")
	public List<RateEntity> getRateOfProduct(@PathVariable(name = "id") String id) {
		return rateService.getRateByProduct(id);
	}
	
	@Operation(summary = "Get a Rate for Product by Pages") 
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "The request has succeeded", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ProductEntity.class)) }),
			@ApiResponse(responseCode = "401", description = "Unauthorized, Need to login first!", content = @Content),
			@ApiResponse(responseCode = "400", description = "Bad Request: Invalid syntax", content = @Content),
			@ApiResponse(responseCode = "404", description = "Can not find the requested resource", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
	@GetMapping("/product/rate")
	public List<RateEntity> getRateProductPages(@RequestParam(name = "pagenum") int page, 
			@RequestParam(name = "size") int size,@RequestParam(name = "id") String id) {
		return rateService.getRateProductPage(id,page,size);
	}
}
