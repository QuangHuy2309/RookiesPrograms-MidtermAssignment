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

import com.nashtech.MyBikeShop.DTO.OrderDTO;
import com.nashtech.MyBikeShop.DTO.ProductDTO;
import com.nashtech.MyBikeShop.entity.OrderEntity;
import com.nashtech.MyBikeShop.entity.ProductEntity;
import com.nashtech.MyBikeShop.service.OrderService;
import com.nashtech.MyBikeShop.service.ProductService;

@RestController
@RequestMapping("/api/order")
public class OrderController {
	@Autowired
	private OrderService orderService;

	@GetMapping
	public List<OrderEntity> retrieveOrders() {
		return  orderService.retrieveOrders();
	}

	@GetMapping("/{id}")
	public OrderEntity findOrder(@PathVariable(name = "id") int id) {
		return orderService.getOrders(id);
	}

	@PostMapping
	public OrderEntity createOrder(@RequestBody OrderDTO newOrder) {
		return orderService.createOrder(newOrder);
	}
//
//	@DeleteMapping("/{id}")
//	public void deletePerson(@PathVariable(name = "id") String id) {
//		productService.deleteProduct(id);
//	}
//
//	@PutMapping
//	public void editPerson(@RequestBody ProductDTO newProduct) {
//		productService.updateProduct(newProduct);
//	}
}
