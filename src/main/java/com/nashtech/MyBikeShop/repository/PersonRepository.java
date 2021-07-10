package com.nashtech.MyBikeShop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nashtech.MyBikeShop.entity.PersonEntity;
import com.nashtech.MyBikeShop.entity.UserEntity;

@Repository
public interface PersonRepository extends JpaRepository<PersonEntity, Integer> {
	List<PersonEntity> findByRole(Pageable pageable, String role);
	PersonEntity findByEmail(String email);
	Boolean existsByEmail(String email);
}
