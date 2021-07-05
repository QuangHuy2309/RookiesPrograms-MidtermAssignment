package com.nashtech.MyBikeShop.services;

import java.util.List;
import java.util.Optional;

import com.nashtech.MyBikeShop.DTO.OrderDTO;
import com.nashtech.MyBikeShop.entity.OrderEntity;


public interface OrderService {
	public List<OrderEntity> retrieveOrders();
	public OrderEntity getOrders(int id);
	public OrderEntity createOrder(OrderDTO order);
	public void deleteOrder(int id);
	public void updateOrder(OrderDTO order);
	public List<OrderEntity> findOrderByCustomer(String email);
	public OrderEntity findOrderByProducts(String id);
}
