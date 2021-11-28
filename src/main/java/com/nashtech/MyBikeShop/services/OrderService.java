package com.nashtech.MyBikeShop.services;

import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;

import com.nashtech.MyBikeShop.DTO.OrderDTO;
import com.nashtech.MyBikeShop.DTO.ProductDTO;
import com.nashtech.MyBikeShop.entity.OrderEntity;
import com.nashtech.MyBikeShop.entity.ProductEntity;

public interface OrderService {
	public List<OrderEntity> retrieveOrders();

	public Optional<OrderEntity> getOrder(int id);

	public List<OrderEntity> getOrdersByCustomerPages(int num, int size, int id);

	public List<OrderEntity> getOrderPage(int num, int size);
	
	public List<OrderEntity> getOrderPageByStatus(int num, int size, int status);
	
	public List<OrderEntity> searchOrderByCustomer(String keyword);
	
	public List<OrderEntity> searchOrderByStatusAndCustomer(String keyword, int status);
	
	public boolean checkOrderedByProductAndCustomerId(String prodId, int customerId);

	public OrderEntity createOrder(OrderDTO order);

	public boolean deleteOrder(int id);

	public boolean updateOrder(OrderDTO order);
	
	public boolean updateOrderPayment(int id, int customerEmail);

	public boolean updateStatusOrder(int id, int status, String userId);

	public List<OrderEntity> getOrderByCustomerEmail(int num, int size, String email);

	public void sendSimpleMessage(String to, String listProd, Double totalCost) throws MessagingException;

	public long countTotal();
	
	public long countByStatus(int status);
	
	public long countTotalOrderByUser(String email);
	
	public float profitByMonth(int month, int year);
	
	public OrderDTO convertToDTO(OrderEntity order);
}
