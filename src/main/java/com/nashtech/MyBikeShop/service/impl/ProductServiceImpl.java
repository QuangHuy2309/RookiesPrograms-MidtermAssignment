package com.nashtech.MyBikeShop.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nashtech.MyBikeShop.DTO.ProductDTO;
import com.nashtech.MyBikeShop.entity.PersonEntity;
import com.nashtech.MyBikeShop.entity.ProductEnity;
import com.nashtech.MyBikeShop.exception.ObjectAlreadyExistException;
import com.nashtech.MyBikeShop.exception.ObjectNotFoundException;
import com.nashtech.MyBikeShop.repository.ProductRepository;
import com.nashtech.MyBikeShop.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService{
	@Autowired
	ProductRepository productRepository;
	
	public ProductServiceImpl(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}
	
	public List<ProductEnity> retrieveProducts(){
		return productRepository.findAll();
		
	}
	public ProductEnity getProduct(String id) {
		return productRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Could not find product with Id: " + id));
		
	}
	public ProductEnity createProduct(ProductDTO productDTO) {
		Optional<ProductEnity> product = productRepository.findById(productDTO.getId());
		if (product.isPresent()) {
			throw new ObjectAlreadyExistException("There is a product with Id " + product.get().getId());
		} else {
			ProductEnity productEntity = new ProductEnity(productDTO);
			return productRepository.save(productEntity);
		}
	}
	public void deleteProduct(String id) {
		productRepository.deleteById(id);
	}
	public void updateProduct(ProductDTO productDTO) {
		ProductEnity product = new ProductEnity(productDTO);
		productRepository.save(product);
	}
}
