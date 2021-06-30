package com.nashtech.MyBikeShop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nashtech.MyBikeShop.entity.PersonEntity;

@Repository
public interface PersonRepository extends JpaRepository<PersonEntity, String>{

}
