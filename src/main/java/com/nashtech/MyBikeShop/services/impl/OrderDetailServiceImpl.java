package com.nashtech.MyBikeShop.services.impl;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nashtech.MyBikeShop.entity.OrderDetailEntity;
import com.nashtech.MyBikeShop.entity.OrderEntity;
import com.nashtech.MyBikeShop.entity.ProductEntity;
import com.nashtech.MyBikeShop.repository.OrderDetailRepository;
import com.nashtech.MyBikeShop.services.OrderDetailService;
import com.nashtech.MyBikeShop.services.OrderService;
import com.nashtech.MyBikeShop.services.ProductService;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {
	@Autowired
	OrderDetailRepository orderDetailRepo;

	@Autowired
	OrderService orderService;

	@Autowired
	ProductService productService;

	public OrderDetailServiceImpl() {
		super();
	}

	public OrderDetailServiceImpl(OrderDetailRepository orderDetailRepo) {
		this.orderDetailRepo = orderDetailRepo;
	}

	public List<OrderDetailEntity> retrieveOrders() {
		return orderDetailRepo.findAll();
	}

	public Set<OrderDetailEntity> getDetailOrderByOrderId(int id) {
		return orderDetailRepo.findByIdOrderId(id);
	}

	@Transactional
	public boolean createDetail(OrderDetailEntity order) {
		boolean result = productService.updateProductQuantity(order.getId().getProductId(), order.getAmmount() * (-1));
		if (!result)
			return false;
		orderDetailRepo.save(order);
		return true;
	}

	@Transactional
	public boolean deleteDetail(OrderDetailEntity orderDetailEntity) {
		int orderId = orderDetailEntity.getId().getOrderId();
		OrderEntity orderEntity = orderService.getOrder(orderId).get();
		if (orderEntity.getStatus() != 4) { // False = Not delivery yet
			boolean result = productService.updateProductQuantity(orderDetailEntity.getId().getProductId(),
					orderDetailEntity.getAmmount());
			if (!result)
				return false;

		}
//		orderDetailRepo.delete(orderDetailEntity);
		return true;
	}

	@Transactional
	public boolean updateDetailCancel(OrderDetailEntity orderDetailEntity) {
//		ProductEntity prod = productService.getProduct(orderDetailEntity.getId().getProductId()).get();
		boolean result = productService.updateProductQuantityToCancel(orderDetailEntity.getId().getProductId(),
				orderDetailEntity.getAmmount());
		if (!result)
			return false;
		return true;
	}

	@Transactional
	public boolean updateDetail(OrderDetailEntity orderDetailEntity) {
		ProductEntity prod = productService.getProduct(orderDetailEntity.getId().getProductId()).get();
		boolean result = productService.updateProductQuantity(orderDetailEntity.getId().getProductId(),
				orderDetailEntity.getAmmount() * (-1));
		if (!result)
			return false;
		return true;
	}
}
