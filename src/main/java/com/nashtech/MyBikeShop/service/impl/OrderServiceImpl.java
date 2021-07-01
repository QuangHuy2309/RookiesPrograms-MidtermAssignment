package com.nashtech.MyBikeShop.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nashtech.MyBikeShop.DTO.OrderDTO;
import com.nashtech.MyBikeShop.entity.OrderEntity;
import com.nashtech.MyBikeShop.entity.ProductEntity;
import com.nashtech.MyBikeShop.exception.ObjectAlreadyExistException;
import com.nashtech.MyBikeShop.exception.ObjectNotFoundException;
import com.nashtech.MyBikeShop.repository.OrderRepository;
import com.nashtech.MyBikeShop.repository.ProductRepository;
import com.nashtech.MyBikeShop.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {
	@Autowired
	OrderRepository orderRepository;

	@Autowired
	ProductRepository productRepository;

	public OrderServiceImpl(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	public List<OrderEntity> retrieveOrders() {
		return orderRepository.findAll();

	}

	public OrderEntity getOrders(int id) {
		return orderRepository.findById(id)
				.orElseThrow(() -> new ObjectNotFoundException("Could not find product with Id: " + id));

	}

	@Transactional
	public OrderEntity createOrder(OrderDTO orderDTO) {
//		Optional<OrderEntity> order = orderRepository.findById(orderDTO.getId());
//		if (order.isPresent()) {
//			throw new ObjectAlreadyExistException("There is a product with Id " + order.get().getId());
//		} else {
			OrderEntity orderEntity = new OrderEntity(orderDTO);
			String prod_id = orderEntity.getProducts().getId();
			ProductEntity product = productRepository.findById(prod_id)
					.orElseThrow(() -> new ObjectNotFoundException("Could not find product with Id: " + prod_id));
			product.decreaseQuantity(orderEntity.getQuantity());
			productRepository.save(product);
			return orderRepository.save(orderEntity);
		
	}
//	public void deleteProduct(String id) {
//		productRepository.deleteById(id);
//	}
//	public void updateProduct(ProductDTO productDTO) {
//		ProductEnity product = new ProductEnity(productDTO);
//		productRepository.save(product);
//	}
}
