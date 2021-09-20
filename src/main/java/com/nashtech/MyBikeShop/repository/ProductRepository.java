package com.nashtech.MyBikeShop.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nashtech.MyBikeShop.entity.ProductEntity;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, String> {
	List<ProductEntity> findByCategoriesId(int id);

	List<ProductEntity> findByCategoriesId(Pageable pageable, int id);

	List<ProductEntity> findByNameIgnoreCase(String name);

	List<ProductEntity> findByIdIgnoreCase(String name);

	int countByCategoriesId(int id);

	boolean existsByName(String name);

	@Query("SELECT p FROM ProductEntity p WHERE UPPER(p.name) LIKE %?1% and (p.categories.id = ?2) ORDER BY p.updateDate DESC")
	List<ProductEntity> searchProduct(String keyword, int type);
	
	@Query("SELECT p FROM ProductEntity p WHERE UPPER(p.name) LIKE %?1% ORDER BY p.updateDate DESC")
	List<ProductEntity> searchProduct(String keyword);
}
