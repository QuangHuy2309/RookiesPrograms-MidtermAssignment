package com.nashtech.MyBikeShop.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nashtech.MyBikeShop.entity.ProductEntity;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, String>{
	List<ProductEntity> findByCategoriesId(int id);
	List<ProductEntity> findByCategoriesId(Pageable pageable, int id);
	List<ProductEntity> findByNameIgnoreCase(String name);
	int countByCategoriesId(int id);
	boolean existsByName(String name);
}
