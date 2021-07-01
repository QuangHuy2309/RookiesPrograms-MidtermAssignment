package com.nashtech.MyBikeShop.service;

import java.util.List;

import com.nashtech.MyBikeShop.DTO.ProductDTO;
import com.nashtech.MyBikeShop.entity.ProductEnity;


public interface ProductService {
	public List<ProductEnity> retrieveProducts();
	public ProductEnity getProduct(String id);
	public ProductEnity createProduct(ProductDTO product);
	public void deleteProduct(String id);
	public void updateProduct(ProductDTO product);
}
