package com.nashtech.MyBikeShop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nashtech.MyBikeShop.entity.CategoriesEntity;

@Repository
public interface CategoriesRepository extends JpaRepository<CategoriesEntity, Integer> {
	List<CategoriesEntity> findByStatusNot(Sort sort, boolean status);
	
	Optional<CategoriesEntity> findByIdAndStatusNot(int id, boolean status);
	
	Boolean existsByNameAndStatusNot(String name, boolean status);

	List<CategoriesEntity> findByNameIgnoreCaseAndStatusNot(String name, boolean status);

	int countByNameAndStatusNot(String name, boolean status);
}
