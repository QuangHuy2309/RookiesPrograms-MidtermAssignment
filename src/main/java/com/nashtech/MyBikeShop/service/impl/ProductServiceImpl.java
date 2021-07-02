package com.nashtech.MyBikeShop.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nashtech.MyBikeShop.DTO.ProductDTO;
import com.nashtech.MyBikeShop.entity.PersonEntity;
import com.nashtech.MyBikeShop.entity.ProductEntity;
import com.nashtech.MyBikeShop.exception.ObjectAlreadyExistException;
import com.nashtech.MyBikeShop.exception.ObjectNotFoundException;
import com.nashtech.MyBikeShop.repository.ProductRepository;
import com.nashtech.MyBikeShop.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	ProductRepository productRepository;

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

	public ProductEntity createProduct(ProductDTO productDTO) {
		Optional<ProductEntity> product = productRepository.findById(productDTO.getId());
		if (product.isPresent()) {
			throw new ObjectAlreadyExistException("There is a product with Id " + product.get().getId());
		} else {
			ProductEntity productEntity = new ProductEntity(productDTO);
			return productRepository.save(productEntity);
		}
	}

	public void deleteProduct(String id) {
		productRepository.deleteById(id);
	}

	public void updateProduct(ProductDTO productDTO) {
		ProductEntity product = new ProductEntity(productDTO);
		productRepository.save(product);
	}

	public void updateProduct(ProductEntity product) {
		productRepository.save(product);
	}
	public void updateProductQuantity(String id, int numberChange) {
		ProductEntity product = getProduct(id);
		product.changeQuantity(numberChange);
		productRepository.save(product);
	}
}
