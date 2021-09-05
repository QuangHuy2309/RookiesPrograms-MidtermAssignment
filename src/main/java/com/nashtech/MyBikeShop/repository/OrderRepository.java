package com.nashtech.MyBikeShop.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nashtech.MyBikeShop.entity.OrderEntity;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {
	List<OrderEntity> findByCustomersEmail(Pageable pageable, String email);

	List<OrderEntity> findByCustomersId(Pageable pageable, int id);
	
	long countByCustomersEmail(String email);
}
