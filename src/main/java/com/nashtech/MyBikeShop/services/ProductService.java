package com.nashtech.MyBikeShop.services;

import java.util.List;

import com.nashtech.MyBikeShop.DTO.ProductDTO;
import com.nashtech.MyBikeShop.entity.ProductEntity;


public interface ProductService {
	public List<ProductEntity> retrieveProducts();
	public ProductEntity getProduct(String id);
	public String createProduct(ProductDTO product);
	public String deleteProduct(String id);
	public void updateProduct(ProductDTO product);
	public void updateProduct(ProductEntity product);
	public void updateProductQuantity(String id, int numberChange);
	public ProductEntity findProductByCategories(int id);
	public ProductEntity updateDate(ProductEntity product);
}
