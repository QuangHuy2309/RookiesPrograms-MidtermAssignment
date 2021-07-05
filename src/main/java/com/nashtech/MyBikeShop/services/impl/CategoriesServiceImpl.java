package com.nashtech.MyBikeShop.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nashtech.MyBikeShop.DTO.CategoriesDTO;
import com.nashtech.MyBikeShop.entity.CategoriesEntity;
import com.nashtech.MyBikeShop.entity.ProductEntity;
import com.nashtech.MyBikeShop.exception.ObjectAlreadyExistException;
import com.nashtech.MyBikeShop.exception.ObjectNotFoundException;
import com.nashtech.MyBikeShop.repository.CategoriesRepository;
import com.nashtech.MyBikeShop.services.CategoriesService;
import com.nashtech.MyBikeShop.services.ProductService;

@Service
public class CategoriesServiceImpl implements CategoriesService{
	@Autowired
	CategoriesRepository categoriesRepository;
	
	@Autowired
	ProductService productService;
	
	public List<CategoriesEntity> retrieveCategories(){
		return categoriesRepository.findAll();
	}
	public CategoriesEntity getCategories(int id) {
		return categoriesRepository.findById(id)
		.orElseThrow(() -> new ObjectNotFoundException("Could not find categories with Id: " + id));
	}
	
	public String createCategories(CategoriesDTO categoriesDTO) {
		CategoriesEntity categoriesFind = categoriesRepository.findByName(categoriesDTO.getName());
		if (categoriesFind == null) {
			CategoriesEntity categoriesConvert = new CategoriesEntity(categoriesDTO);
			categoriesRepository.save(categoriesConvert);
			return "Success";
		}
		else return "There is a category with the same name";
	}
	public String deleteCategories(int id) {
		try {
			categoriesRepository.deleteById(id);
			return "Success";
		}
		catch(Exception ex){
			return "There is a product use this categories.\n" + ex.getMessage();
		}
	}
	public String updateCategories(CategoriesDTO categories) {
		return "Success";
	}
}
