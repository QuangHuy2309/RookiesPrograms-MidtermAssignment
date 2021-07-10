package com.nashtech.MyBikeShop.services;

import java.util.List;
import java.util.Optional;

import com.nashtech.MyBikeShop.DTO.ProductDTO;
import com.nashtech.MyBikeShop.entity.ProductEntity;


public interface ProductService {
	public List<ProductEntity> retrieveProducts();
	public List<ProductEntity> getProductPage(int num, int size);
	public Optional<ProductEntity> getProduct(String id);
	public ProductEntity createProduct(ProductDTO product);
	public boolean deleteProduct(String id);
	public boolean updateProduct(ProductDTO product);
	public boolean updateProductQuantity(String id, int numberChange);
	public List<ProductEntity> findProductByCategories(int id);
	public ProductEntity updateDate(ProductEntity product);
}
