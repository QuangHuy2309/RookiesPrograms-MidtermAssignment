package com.nashtech.MyBikeShop.services;

import java.util.List;
import java.util.Optional;

import com.nashtech.MyBikeShop.DTO.OrderDTO;
import com.nashtech.MyBikeShop.entity.OrderEntity;


public interface OrderService {
	public List<OrderEntity> retrieveOrders();
	public Optional<OrderEntity> getOrders(int id);
	public List<OrderEntity> getOrdersByCustomerPages(int num, int size, int id);
	public List<OrderEntity> getOrderPage(int num, int size);
	public boolean createOrder(OrderDTO order);
	public boolean deleteOrder(int id);
	public boolean updateOrder(OrderDTO order);
	public List<OrderEntity> findOrderByCustomer(int num, int size, String email);
//	public OrderEntity findOrderByProducts(String id);
}
