package com.nashtech.MyBikeShop.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nashtech.MyBikeShop.entity.OrderDetailEntity;
import com.nashtech.MyBikeShop.entity.OrderDetailEntity.OrderDetailsKey;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetailEntity, OrderDetailsKey> {
	Set<OrderDetailEntity> findByIdOrderId(int id);
}
