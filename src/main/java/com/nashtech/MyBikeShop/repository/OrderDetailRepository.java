package com.nashtech.MyBikeShop.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nashtech.MyBikeShop.entity.OrderDetailEntity;
import com.nashtech.MyBikeShop.entity.OrderDetailEntity.OrderDetailsKey;
import com.nashtech.MyBikeShop.entity.OrderEntity;
import com.nashtech.MyBikeShop.entity.PersonEntity;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetailEntity, OrderDetailsKey> {
	//@Query(nativeQuery = true, value="select o from OrderEntity o where o.customers.getEmail() = ':email'")
	//List<OrderEntity> findByCustomersEmail(String email);
	Set<OrderDetailEntity> findByIdOrderId(int id);
}
