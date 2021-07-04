package com.nashtech.MyBikeShop.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nashtech.MyBikeShop.entity.PersonEntity;
import com.nashtech.MyBikeShop.entity.UserEntity;

@Repository
public interface PersonRepository extends JpaRepository<PersonEntity, String> {
	Optional<UserEntity> findByEmail(String email);

	Boolean existsByEmail(String email);
}
