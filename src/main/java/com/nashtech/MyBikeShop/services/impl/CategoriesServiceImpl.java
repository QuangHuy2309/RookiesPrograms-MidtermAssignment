package com.nashtech.MyBikeShop.services.impl;

import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.nashtech.MyBikeShop.DTO.CategoriesDTO;
import com.nashtech.MyBikeShop.entity.CategoriesEntity;
import com.nashtech.MyBikeShop.entity.ProductEntity;
import com.nashtech.MyBikeShop.exception.ObjectAlreadyExistException;
import com.nashtech.MyBikeShop.exception.ObjectContainNullException;
import com.nashtech.MyBikeShop.exception.ObjectNotFoundException;
import com.nashtech.MyBikeShop.exception.ObjectViolateForeignKeyException;
import com.nashtech.MyBikeShop.repository.CategoriesRepository;
import com.nashtech.MyBikeShop.services.CategoriesService;
import com.nashtech.MyBikeShop.services.ProductService;

@Service
public class CategoriesServiceImpl implements CategoriesService {
	@Autowired
	CategoriesRepository categoriesRepository;

	@Autowired
	ProductService productService;

	public List<CategoriesEntity> retrieveCategories() {
		return categoriesRepository.findAll();
	}

	public Optional<CategoriesEntity> getCategories(int id) {
		return categoriesRepository.findById(id);
	}

	public boolean createCategories(CategoriesDTO categoriesDTO) {
		try {
			CategoriesEntity categoriesFind = categoriesRepository.findByName(categoriesDTO.getName());
			if (categoriesFind == null) {
				CategoriesEntity categoriesConvert = new CategoriesEntity(categoriesDTO);
				categoriesRepository.save(categoriesConvert);
				return true;
			} else
				throw new ObjectAlreadyExistException("There is a category with the same Name");
		} catch (IllegalArgumentException | ConstraintViolationException ex) {
			return false;
			//throw new ObjectContainNullException(ex.getMessage());
		}

	}

	public boolean deleteCategories(int id) {
//		try {
			categoriesRepository.deleteById(id);
			return false;
//		catch (EmptyResultDataAccessException | DataIntegrityViolationException ex) {		
//			throw new ObjectNotFoundException("No product found to delete!");
//			return false;
//		}
	}
//	public boolean updateCategories(CategoriesDTO categories) {
//		return createCategories(categories);
//	}
}
