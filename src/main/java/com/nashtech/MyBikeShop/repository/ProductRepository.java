package com.nashtech.MyBikeShop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nashtech.MyBikeShop.entity.ProductEnity;

@Repository
public interface ProductRepository extends JpaRepository<ProductEnity, String>{

}
