package com.nashtech.MyBikeShop.service;

import java.util.List;

import com.nashtech.MyBikeShop.DTO.ProductDTO;
import com.nashtech.MyBikeShop.entity.ProductEntity;


public interface ProductService {
	public List<ProductEntity> retrieveProducts();
	public ProductEntity getProduct(String id);
	public ProductEntity createProduct(ProductDTO product);
	public void deleteProduct(String id);
	public void updateProduct(ProductDTO product);
	public void updateProduct(ProductEntity product);
	public void updateProductQuantity(String id, int numberChange);
}
