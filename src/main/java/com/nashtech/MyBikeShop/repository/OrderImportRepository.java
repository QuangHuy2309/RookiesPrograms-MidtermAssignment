package com.nashtech.MyBikeShop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nashtech.MyBikeShop.entity.OrderImportEntity;

@Repository
public interface OrderImportRepository extends JpaRepository<OrderImportEntity, Integer>{
	
}
