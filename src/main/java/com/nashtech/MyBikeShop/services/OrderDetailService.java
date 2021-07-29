package com.nashtech.MyBikeShop.services;

import java.util.List;
import java.util.Set;

import com.nashtech.MyBikeShop.entity.OrderDetailEntity;

public interface OrderDetailService {
	public List<OrderDetailEntity> retrieveOrders();

	public Set<OrderDetailEntity> getDetailOrderByOrderId(int id);

	public boolean createDetail(OrderDetailEntity orDetail);

	public boolean deleteDetail(OrderDetailEntity orderDetailEntity);
}
