package com.nashtech.MyBikeShop.services;

import java.util.List;
import java.util.Optional;

import com.nashtech.MyBikeShop.DTO.CategoriesDTO;
import com.nashtech.MyBikeShop.entity.CategoriesEntity;
import com.nashtech.MyBikeShop.exception.ObjectAlreadyExistException;

public interface CategoriesService {
	public List<CategoriesEntity> retrieveCategories();
	public Optional<CategoriesEntity> getCategories(int id);
	public boolean createCategories(CategoriesDTO categories);
	public boolean deleteCategories(int id);
	public boolean checkExistName(int id, String name);
	public boolean updateCategories(CategoriesDTO categories);
}
