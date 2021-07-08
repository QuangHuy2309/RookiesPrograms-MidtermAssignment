package com.nashtech.MyBikeShop.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.nashtech.MyBikeShop.DTO.OrderDTO;
import com.nashtech.MyBikeShop.DTO.OrderDetailDTO;
import com.nashtech.MyBikeShop.entity.OrderDetailEntity;
import com.nashtech.MyBikeShop.entity.OrderDetailEntity.OrderDetailsKey;
import com.nashtech.MyBikeShop.entity.OrderEntity;


public interface OrderDetailService {
	public List<OrderDetailEntity> retrieveOrders();
	public Set<OrderDetailEntity> getDetailOrder(int id);
	public OrderDetailEntity createDetail(OrderDetailEntity orDetail);
	public String deleteDetail(OrderDetailEntity OrderDetailEntity);
	//public void updateOrder(OrderDetailDTO orDetail);
	//public List<OrderDetailEntity> findOrderByCustomer(String email);
	//public OrderDetailEntity findOrderByProducts(String id);
}
