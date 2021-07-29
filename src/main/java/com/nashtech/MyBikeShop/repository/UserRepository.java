package com.nashtech.MyBikeShop.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nashtech.MyBikeShop.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

	Optional<UserEntity> findByEmail(String email);

	Boolean existsByEmail(String email);
}
