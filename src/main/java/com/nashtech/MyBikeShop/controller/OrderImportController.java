package com.nashtech.MyBikeShop.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import com.nashtech.MyBikeShop.DTO.OrderImportDTO;
import com.nashtech.MyBikeShop.DTO.OrderImportDetailDTO;
import com.nashtech.MyBikeShop.entity.OrderImportDetailEntity;
import com.nashtech.MyBikeShop.entity.OrderImportEntity;
import com.nashtech.MyBikeShop.entity.PersonEntity;
import com.nashtech.MyBikeShop.entity.ProductEntity;
import com.nashtech.MyBikeShop.exception.ObjectNotFoundException;
import com.nashtech.MyBikeShop.payload.response.MessageResponse;
import com.nashtech.MyBikeShop.services.OrderImportDetailService;
import com.nashtech.MyBikeShop.services.OrderImportService;
import com.nashtech.MyBikeShop.services.PersonService;
import com.nashtech.MyBikeShop.services.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class OrderImportController {

	@Autowired
	OrderImportService orderImportService;

	@Autowired
	OrderImportDetailService importDetailService;

	@Autowired
	ProductService productService;

	@Autowired
	PersonService personService;

	@Operation(summary = "Create Order import")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "The request has succeeded", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = OrderImportDTO.class)) }),
			@ApiResponse(responseCode = "401", description = "Unauthorized, Need to login first!", content = @Content),
			@ApiResponse(responseCode = "400", description = "Bad Request: Invalid syntax", content = @Content),
			@ApiResponse(responseCode = "404", description = "Can not find the requested resource", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
	@PostMapping("/imports")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> createOrderImport(@RequestBody OrderImportDTO newOrderImportDto) {
		PersonEntity personImport = personService.getPerson(newOrderImportDto.getEmployeeEmail());
		if (personImport == null) {
			throw new ObjectNotFoundException("Employee not found");
		}
		Set<OrderImportDetailDTO> importDetailDtoList = newOrderImportDto.getOrderImportDetails();
		Set<OrderImportDetailEntity> importDetailEntityList = new HashSet<OrderImportDetailEntity>();
		OrderImportEntity orderImport = orderImportService.convertToEntity(newOrderImportDto);
		for (OrderImportDetailDTO detailDto : importDetailDtoList) {
			ProductEntity product = productService.getProduct(detailDto.getProductId()).orElse(null);
			if (product == null) {
				throw new ObjectNotFoundException("Product ID " + detailDto.getProductId() + " not found!");
			}
			OrderImportDetailEntity detailEntity = importDetailService.convertToEntity(detailDto);
			detailEntity.setOrder(orderImport);
			importDetailEntityList.add(detailEntity);
		}

		orderImport.setOrderImportDetails(importDetailEntityList);
		
		OrderImportEntity orderImportCreated =  orderImportService.createOrderImport(orderImport);
		
		return new ResponseEntity<OrderImportDTO>(orderImportService.convertToDto(orderImportCreated), HttpStatus.OK);
		
	}

	@Operation(summary = "Get Order import")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "The request has succeeded", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = OrderImportDTO.class)) }),
			@ApiResponse(responseCode = "401", description = "Unauthorized, Need to login first!", content = @Content),
			@ApiResponse(responseCode = "400", description = "Bad Request: Invalid syntax", content = @Content),
			@ApiResponse(responseCode = "404", description = "Can not find the requested resource", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
	@GetMapping("/imports")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getOrderImport(@RequestParam(name = "pagenum") int page,
			@RequestParam(name = "size") int size) {
		List<OrderImportEntity> orderImportEntity = orderImportService.getOrderImportPage(page, size);
		List<OrderImportDTO> orderImportDto = orderImportEntity.stream().map(orderImportService::convertToDto)
				.collect(Collectors.toList());
		return new ResponseEntity<List<OrderImportDTO>>(orderImportDto, HttpStatus.OK);
	}

	@Operation(summary = "Get Order import detail")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "The request has succeeded", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = OrderImportDTO.class)) }),
			@ApiResponse(responseCode = "401", description = "Unauthorized, Need to login first!", content = @Content),
			@ApiResponse(responseCode = "400", description = "Bad Request: Invalid syntax", content = @Content),
			@ApiResponse(responseCode = "404", description = "Can not find the requested resource", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
	@GetMapping("/imports/{importId}")
	@PreAuthorize(" hasRole('ADMIN')")
	public ResponseEntity<?> getOrderImportDetail(@PathVariable int importId) {
		OrderImportEntity orderImport = orderImportService.findOrderImportById(importId);
		if (orderImport == null) {
			throw new ObjectNotFoundException("Order import not found!");
		}
		return new ResponseEntity<OrderImportDTO>(orderImportService.convertToDto(orderImport), HttpStatus.OK);
	}

	@Operation(summary = "Update Order import")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "The request has succeeded", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = OrderImportDTO.class)) }),
			@ApiResponse(responseCode = "401", description = "Unauthorized, Need to login first!", content = @Content),
			@ApiResponse(responseCode = "400", description = "Bad Request: Invalid syntax", content = @Content),
			@ApiResponse(responseCode = "404", description = "Can not find the requested resource", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
	@PutMapping("/imports/{importId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> updateOrder(@RequestBody OrderImportDTO orderImportDto, @PathVariable(name = "importId") int importId) {
		OrderImportEntity orderImport = orderImportService.findOrderImportById(importId);
		if(orderImport == null) {
			throw new ObjectNotFoundException("Order import not found!");
		}
		
		OrderImportEntity orderImportUpdate = orderImportService.updateOrderImport(orderImportDto, importId);
		if (orderImportUpdate == null) {
			return ResponseEntity.internalServerError().body(new MessageResponse("Update order import fail!"));
		}
		
		return new ResponseEntity<OrderImportDTO>(orderImportService.convertToDto(orderImportUpdate), HttpStatus.OK);
	}
	
	@Operation(summary = "Delete Order import by ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "The request has succeeded", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = OrderImportDTO.class)) }),
			@ApiResponse(responseCode = "401", description = "Unauthorized, Need to login first!", content = @Content),
			@ApiResponse(responseCode = "400", description = "Bad Request: Invalid syntax", content = @Content),
			@ApiResponse(responseCode = "404", description = "Can not find the requested resource", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
	@DeleteMapping("/imports/{importId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteOrderImport (@PathVariable(name = "importId") int importId) {
		OrderImportEntity orderImport = orderImportService.findOrderImportById(importId);
		if(orderImport == null) {
			throw new ObjectNotFoundException("Order import not found!");
		}
		if(orderImport.isStatus()) {
			return ResponseEntity.badRequest().body(new MessageResponse("Import order is delivered. Cannot delete!"));
		}
		boolean result = orderImportService.deleteOrderImport(importId);
		if(!result) {
			return ResponseEntity.internalServerError().body(new MessageResponse("Delete fail!"));
		}
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
}
