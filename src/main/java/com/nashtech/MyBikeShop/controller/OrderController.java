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

import com.nashtech.MyBikeShop.DTO.OrderDTO;
import com.nashtech.MyBikeShop.DTO.ProductDTO;
import com.nashtech.MyBikeShop.entity.OrderEntity;
import com.nashtech.MyBikeShop.entity.ProductEntity;
import com.nashtech.MyBikeShop.services.OrderService;
import com.nashtech.MyBikeShop.services.ProductService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/order")
public class OrderController {
	@Autowired
	private OrderService orderService;

	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public List<OrderEntity> retrieveOrders() {
		return  orderService.retrieveOrders();
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public OrderEntity findOrder(@PathVariable(name = "id") int id) {
		return orderService.getOrders(id);
	}

	@PostMapping
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public OrderEntity createOrder(@RequestBody OrderDTO newOrder) {
		return orderService.createOrder(newOrder);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public void deleteOrder(@PathVariable(name = "id") int id) {
		orderService.deleteOrder(id);
	}

	@PutMapping
	@PreAuthorize("hasRole('ADMIN')")
	public void updateOrder(@RequestBody OrderDTO order) {
		orderService.updateOrder(order);
	}
}
