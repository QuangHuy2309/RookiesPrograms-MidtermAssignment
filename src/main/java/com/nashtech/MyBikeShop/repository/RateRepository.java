package com.nashtech.MyBikeShop.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nashtech.MyBikeShop.entity.RateEntity;
import com.nashtech.MyBikeShop.entity.RateEntity.RateKey;

@Repository
public interface RateRepository extends JpaRepository<RateEntity,RateKey>{
	List<RateEntity> findByIdProductId (String id);
}
