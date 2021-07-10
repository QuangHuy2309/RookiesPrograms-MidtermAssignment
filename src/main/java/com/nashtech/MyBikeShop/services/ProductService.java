package com.nashtech.MyBikeShop.services;

import java.util.List;
import java.util.Optional;

import com.nashtech.MyBikeShop.DTO.ProductDTO;
import com.nashtech.MyBikeShop.entity.ProductEntity;


public interface ProductService {
	public List<ProductEntity> retrieveProducts();
	public Optional<ProductEntity> getProduct(String id);
	public ProductEntity createProduct(ProductDTO product);
	public boolean deleteProduct(String id);
	public boolean updateProduct(ProductDTO product);
	public boolean updateProductQuantity(String id, int numberChange);
	public ProductEntity findProductByCategories(int id);
	public ProductEntity updateDate(ProductEntity product);
}
