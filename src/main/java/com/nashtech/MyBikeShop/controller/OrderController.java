package com.nashtech.MyBikeShop.controller;

import java.util.List;
import java.util.NoSuchElementException;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nashtech.MyBikeShop.DTO.OrderDTO;
import com.nashtech.MyBikeShop.DTO.ProductDTO;
import com.nashtech.MyBikeShop.Utils.StringUtils;
import com.nashtech.MyBikeShop.entity.CategoriesEntity;
import com.nashtech.MyBikeShop.entity.OrderEntity;
import com.nashtech.MyBikeShop.entity.ProductEntity;
import com.nashtech.MyBikeShop.exception.ObjectNotFoundException;
import com.nashtech.MyBikeShop.services.OrderService;
import com.nashtech.MyBikeShop.services.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/order")
public class OrderController {
	@Autowired
	private OrderService orderService;

//	@Operation(summary = "Get All Order")
//	@ApiResponses(value = {
//			@ApiResponse(responseCode = "200", description = "The request has succeeded", 
//			content = { @Content(mediaType = "application/json", 
//			schema = @Schema(implementation = OrderEntity.class))}),
//			@ApiResponse(responseCode = "401", description = "Unauthorized, Need to login first!", 
//			content = @Content),
//			@ApiResponse(responseCode = "400", description = "Bad Request: Invalid syntax", 
//			content = @Content),
//			@ApiResponse(responseCode = "404", description = "Can not find the requested resource", 
//			content = @Content),
//			@ApiResponse(responseCode = "500", description = "Internal Server Error", 
//			content = @Content)
//	})
//	@GetMapping
//	@PreAuthorize("hasRole('ADMIN')")
//	public List<OrderEntity> retrieveOrders() {
//		return  orderService.retrieveOrders();
//	}

//	@Operation(summary = "Get Order by ID")
//	@ApiResponses(value = {
//			@ApiResponse(responseCode = "200", description = "The request has succeeded", 
//			content = { @Content(mediaType = "application/json", 
//			schema = @Schema(implementation = OrderEntity.class))}),
//			@ApiResponse(responseCode = "401", description = "Unauthorized, Need to login first!", 
//			content = @Content),
//			@ApiResponse(responseCode = "400", description = "Bad Request: Invalid syntax", 
//			content = @Content),
//			@ApiResponse(responseCode = "404", description = "Can not find the requested resource", 
//			content = @Content),
//			@ApiResponse(responseCode = "500", description = "Internal Server Error", 
//			content = @Content)
//	})
	@GetMapping("/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public OrderEntity findOrder(@PathVariable(name = "id") int id) {
		return orderService.getOrders(id)
				.orElseThrow(() -> new ObjectNotFoundException("Could not find order with Id: " + id));
	}

	@Operation(summary = "Create Order")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "The request has succeeded", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = OrderEntity.class)) }),
			@ApiResponse(responseCode = "401", description = "Unauthorized, Need to login first!", content = @Content),
			@ApiResponse(responseCode = "400", description = "Bad Request: Invalid syntax", content = @Content),
			@ApiResponse(responseCode = "404", description = "Can not find the requested resource", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
	@PostMapping
	@PreAuthorize("hasRole('USER')")
	public OrderEntity createOrder(@RequestBody OrderDTO newOrder) {
		return orderService.createOrder(newOrder);
	}

	@Operation(summary = "Delete Order by ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "The request has succeeded", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = OrderEntity.class)) }),
			@ApiResponse(responseCode = "401", description = "Unauthorized, Need to login first!", content = @Content),
			@ApiResponse(responseCode = "400", description = "Bad Request: Invalid syntax", content = @Content),
			@ApiResponse(responseCode = "404", description = "Can not find the requested resource", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public String deleteOrder(@PathVariable(name = "id") int id) {
		try {
			String result = orderService.deleteOrder(id) ? StringUtils.TRUE : StringUtils.FALSE;
			return result;
		} catch (NoSuchElementException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
		}
	}

	@Operation(summary = "Update Order")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "The request has succeeded", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = OrderEntity.class)) }),
			@ApiResponse(responseCode = "401", description = "Unauthorized, Need to login first!", content = @Content),
			@ApiResponse(responseCode = "400", description = "Bad Request: Invalid syntax", content = @Content),
			@ApiResponse(responseCode = "404", description = "Can not find the requested resource", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
	@PutMapping
	@PreAuthorize("hasRole('ADMIN')")
	public String updateOrder(@RequestBody OrderDTO order) {
		try {
			return orderService.updateOrder(order) ? StringUtils.TRUE : StringUtils.FALSE;
		} catch (NoSuchElementException ex) {
			System.err.println(ex.getMessage());
			throw new ObjectNotFoundException("Could not find Order with id: " + order.getId());
		}

	}

	@Operation(summary = "Get Order by Customer")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "The request has succeeded", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = OrderEntity.class)) }),
			@ApiResponse(responseCode = "401", description = "Unauthorized, Need to login first!", content = @Content),
			@ApiResponse(responseCode = "400", description = "Bad Request: Invalid syntax", content = @Content),
			@ApiResponse(responseCode = "404", description = "Can not find the requested resource", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
	@GetMapping("/customeremail")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public List<OrderEntity> findListOrderedByCustomerEmail(@RequestParam(name = "pagenum") int page,
			@RequestParam(name = "size") int size, @RequestParam(name = "email") String email) {
		return orderService.findOrderByCustomer(page, size, email);
	}

	@Operation(summary = "Get Order by CustomerID ")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "The request has succeeded", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = OrderEntity.class)) }),
			@ApiResponse(responseCode = "401", description = "Unauthorized, Need to login first!", content = @Content),
			@ApiResponse(responseCode = "400", description = "Bad Request: Invalid syntax", content = @Content),
			@ApiResponse(responseCode = "404", description = "Can not find the requested resource", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
	@GetMapping("/customerid")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public List<OrderEntity> findListOrderedByCustomerId(@RequestParam(name = "pagenum") int page,
			@RequestParam(name = "size") int size, @RequestParam(name = "id") int id) {
		return orderService.getOrdersByCustomerPages(page, size, id);
	}

	@Operation(summary = "Get Order by OrderId")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "The request has succeeded", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = OrderEntity.class)) }),
			@ApiResponse(responseCode = "401", description = "Unauthorized, Need to login first!", content = @Content),
			@ApiResponse(responseCode = "400", description = "Bad Request: Invalid syntax", content = @Content),
			@ApiResponse(responseCode = "404", description = "Can not find the requested resource", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
	@GetMapping
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public List<OrderEntity> getOrder(@RequestParam(name = "pagenum") int page, @RequestParam(name = "size") int size) {
		return orderService.getOrderPage(page, size);
	}
	
	@PutMapping("updateStatus/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public String updateStatusOrder(@PathVariable(name = "id") int id) {
		try {
			String result = orderService.updateStatusOrder(id) ? StringUtils.TRUE : StringUtils.FALSE;
			return result;
		} catch (NoSuchElementException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
		}
	}
}
