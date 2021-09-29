package com.nashtech.MyBikeShop.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nashtech.MyBikeShop.entity.ProductEntity;
import com.nashtech.MyBikeShop.entity.RateEntity;
import com.nashtech.MyBikeShop.entity.RateEntity.RateKey;

@Repository
public interface RateRepository extends JpaRepository<RateEntity, RateKey> {
	List<RateEntity> findByIdProductIdAndCustomerStatusNot(String id, boolean status);

	List<RateEntity> findByIdProductIdAndCustomerStatusNot(Pageable pageable, String id, boolean status);

	int countByIdProductIdAndCustomerStatusNot(String id, boolean status);

	boolean existsById(RateKey id);

	@Query("select count(o) from PersonEntity p , OrderEntity o , OrderDetailEntity o2, ProductEntity p2 \r\n"
			+ "where p.id = o.customers.id and o2.id.orderId = o.id and o2.id.productId = p2.id and p.status != false "
			+ "and p2.status != false and p2.id = ?1 and p.id = ?2")
	int checkUserOrdered(String prodId, int customerId);
}
