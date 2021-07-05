package com.nashtech.MyBikeShop.services;

import java.util.List;

import com.nashtech.MyBikeShop.DTO.CategoriesDTO;
import com.nashtech.MyBikeShop.entity.CategoriesEntity;

public interface CategoriesService {
	public List<CategoriesEntity> retrieveCategories();
	public CategoriesEntity getCategories(int id);
	public String createCategories(CategoriesDTO categories);
	public String deleteCategories(int id);
	public String updateCategories(CategoriesDTO categories);
}
