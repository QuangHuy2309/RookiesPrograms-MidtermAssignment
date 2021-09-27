package com.nashtech.MyBikeShop.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nashtech.MyBikeShop.entity.OrderEntity;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {
	List<OrderEntity> findByCustomersEmail(Pageable pageable, String email);

	List<OrderEntity> findByCustomersId(Pageable pageable, int id);

	List<OrderEntity> findByStatus(Pageable pageable, int status);

	long countByCustomersEmail(String email);

	long countByStatus(int status);

	@Query(value = "select SUM(o2.amount*o2.unitprice) from orderbill o , orderdetails o2 \r\n"
			+ "where o.id =o2.orderid and EXTRACT(MONTH FROM o.timebought) = :month and EXTRACT(YEAR FROM o.timebought) = :year", nativeQuery = true)
	Float profitByMonth(@Param("month") Integer month, @Param("year") Integer year);
}
