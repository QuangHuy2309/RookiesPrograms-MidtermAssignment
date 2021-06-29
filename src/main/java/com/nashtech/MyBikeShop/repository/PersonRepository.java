package com.nashtech.MyBikeShop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nashtech.MyBikeShop.entity.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, String>{

}
