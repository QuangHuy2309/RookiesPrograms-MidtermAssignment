package com.nashtech.MyBikeShop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nashtech.MyBikeShop.entity.OrderImportEntity;

@Repository
public interface OrderImportRepository extends JpaRepository<OrderImportEntity, Integer> {
	@Query(value = "select SUM(totalcost) from orderimport o where EXTRACT(MONTH FROM o.timeimport) = :month and EXTRACT(YEAR FROM o.timeimport) = :year", nativeQuery = true)
	Float purchaseCostByMonth(@Param("month") Integer month, @Param("year") Integer year);
}
