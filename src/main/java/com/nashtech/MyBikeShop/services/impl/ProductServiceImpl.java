package com.nashtech.MyBikeShop.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.nashtech.MyBikeShop.DTO.ProductDTO;
import com.nashtech.MyBikeShop.entity.OrderEntity;
import com.nashtech.MyBikeShop.entity.PersonEntity;
import com.nashtech.MyBikeShop.entity.ProductEntity;
import com.nashtech.MyBikeShop.exception.ObjectAlreadyExistException;
import com.nashtech.MyBikeShop.exception.ObjectNotFoundException;
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

	public ProductEntity getProduct(String id) {
		return productRepository.findById(id)
				.orElseThrow(() -> new ObjectNotFoundException("Could not find product with Id: " + id));

	}

	public String createProduct(ProductDTO productDTO) {
		ProductEntity productCheck = productRepository.findById(productDTO.getId()).get();
		if (productCheck == null) {
			ProductEntity productEntity = new ProductEntity(productDTO);
			productEntity.setCreateDate(LocalDateTime.now());
			productEntity.setUpdateDate(LocalDateTime.now());
			productRepository.save(productEntity);
			return "Success";
		}
		else throw new ObjectAlreadyExistException("Failed! There is a product with this id. Please change product ID");
	}

	public String deleteProduct(String id) {
		try {
			productRepository.deleteById(id);
			return "Success";
		} catch (DataIntegrityViolationException  ex) {
			throw new  ObjectViolateForeignKeyException("Failed! This Product is existing in an Order. Please delete that Order first");
		}
		catch(EmptyResultDataAccessException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
		}
	}

	public void updateProduct(ProductDTO productDTO) {
		ProductEntity product = new ProductEntity(productDTO);
		
		productRepository.save(updateDate(product));
	}

	public void updateProduct(ProductEntity product) {
		productRepository.save(updateDate(product));
	}

	public void updateProductQuantity(String id, int numberChange) {
		ProductEntity product = getProduct(id);
		product.changeQuantity(numberChange);
		productRepository.save(updateDate(product));
	}

	public ProductEntity findProductByCategories(int id) {
		return productRepository.findByCategoriesId(id);
	}
	public ProductEntity updateDate(ProductEntity product) {
		product.setUpdateDate(LocalDateTime.now());
		return product;
	}
}
