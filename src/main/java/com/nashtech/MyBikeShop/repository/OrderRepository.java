package com.nashtech.MyBikeShop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nashtech.MyBikeShop.entity.OrderEntity;
import com.nashtech.MyBikeShop.entity.PersonEntity;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {
	//@Query(nativeQuery = true, value="select o from OrderEntity o where o.customers.getEmail() = ':email'")
	List<OrderEntity> findByCustomersEmail(String email);
}
