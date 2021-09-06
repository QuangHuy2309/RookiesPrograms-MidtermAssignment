package com.nashtech.MyBikeShop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nashtech.MyBikeShop.entity.OrderDetailEntity.OrderDetailsKey;
import com.nashtech.MyBikeShop.entity.OrderImportDetailEntity;

@Repository
public interface OrderImportDetailRepository extends JpaRepository<OrderImportDetailEntity, OrderDetailsKey>{

}
