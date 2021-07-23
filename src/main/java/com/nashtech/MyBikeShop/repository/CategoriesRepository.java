package com.nashtech.MyBikeShop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nashtech.MyBikeShop.entity.CategoriesEntity;

@Repository
public interface CategoriesRepository extends JpaRepository<CategoriesEntity, Integer> {
	Boolean existsByName(String name);
	CategoriesEntity findByName(String name);
	int countByName(String name);
}
