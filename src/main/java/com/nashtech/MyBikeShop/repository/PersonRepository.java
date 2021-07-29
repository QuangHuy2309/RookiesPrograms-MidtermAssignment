package com.nashtech.MyBikeShop.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nashtech.MyBikeShop.entity.PersonEntity;

@Repository
public interface PersonRepository extends JpaRepository<PersonEntity, Integer> {
	List<PersonEntity> findByRole(Pageable pageable, String role);

	List<PersonEntity> findByEmailIgnoreCase(String email);

	PersonEntity findByEmail(String email);

	Boolean existsByEmail(String email);

	int countByRole(String role);
}
