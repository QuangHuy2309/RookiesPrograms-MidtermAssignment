package com.nashtech.MyBikeShop.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;

import com.nashtech.MyBikeShop.DTO.ProductDTO;
import com.nashtech.MyBikeShop.entity.OrderEntity;
import com.nashtech.MyBikeShop.entity.PersonEntity;
import com.nashtech.MyBikeShop.entity.ProductEntity;
import com.nashtech.MyBikeShop.exception.ObjectAlreadyExistException;
import com.nashtech.MyBikeShop.exception.ObjectNotFoundException;
import com.nashtech.MyBikeShop.exception.ObjectPropertiesIllegalException;
import com.nashtech.MyBikeShop.exception.ObjectViolateForeignKeyException;
import com.nashtech.MyBikeShop.repository.ProductRepository;
import com.nashtech.MyBikeShop.services.OrderService;
import com.nashtech.MyBikeShop.services.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	ProductRepository productRepository;

	@Autowired
	OrderService orderService;

	public ProductServiceImpl(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public List<ProductEntity> retrieveProducts() {
		return productRepository.findAll();

	}

	public Optional<ProductEntity> getProduct(String id) {
		return productRepository.findById(id);
	}

	public ProductEntity createProduct(ProductDTO productDTO) {
		try {
			ProductEntity productCheck = productRepository.findById(productDTO.getId()).orElse(null);
			if (productCheck == null) {
				ProductEntity productEntity = new ProductEntity(productDTO);
				productEntity.setCreateDate(LocalDateTime.now());
				productEntity.setUpdateDate(LocalDateTime.now());
				return productRepository.save(productEntity);
			} else
				throw new ObjectAlreadyExistException(
						"Failed! There is a product with this id. Please change product ID");
		}
		catch (DataAccessException ex) {
			throw new ObjectNotFoundException("Failed!"+ ex.getMessage());
		} 
		catch (IllegalArgumentException ex) {
			throw new ObjectPropertiesIllegalException("Failed!"+ ex.getMessage());
		}
	}

	public boolean deleteProduct(String id) {
			productRepository.deleteById(id);
			return true;
	}

	public boolean updateProduct(ProductDTO productDTO) {
		ProductEntity product = new ProductEntity(productDTO);
		productRepository.save(updateDate(product));
		return true;
	}


	public boolean updateProductQuantity(String id, int numberChange) {
		try {
		ProductEntity product = getProduct(id).get();
		product.changeQuantity(numberChange);
		//productRepository.save(updateDate(product));
		return true;
		}
		catch (NoSuchElementException ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public List<ProductEntity> findProductByCategories(int id) {
		return productRepository.findByCategoriesId(id);
	}

	public ProductEntity updateDate(ProductEntity product) {
		product.setUpdateDate(LocalDateTime.now());
		return product;
	}
}
