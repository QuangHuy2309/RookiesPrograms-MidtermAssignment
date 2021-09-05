package com.nashtech.MyBikeShop.services;

import java.util.List;
import java.util.Optional;

import com.nashtech.MyBikeShop.DTO.ProductDTO;
import com.nashtech.MyBikeShop.entity.ProductEntity;

public interface ProductService {
	public List<ProductEntity> retrieveProducts();
	
	public List<ProductEntity> retrieveProductsByType(int categoriesId);

	public List<ProductEntity> getProductPage(int num, int size, int categoriesId);

	public List<ProductEntity> getProductPageWithSort(int num, int size, int categoriesId, String sortType);

	public List<ProductEntity> getNewestProductCategories(int idCategrories, int size);

	public Optional<ProductEntity> getProduct(String id);

	public ProductEntity createProduct(ProductDTO product);

	public boolean deleteProduct(String id);

	public boolean updateProduct(ProductDTO product);

	public boolean updateProductQuantity(String id, int numberChange);

	public List<ProductEntity> findProductByCategories(int id);

	public ProductEntity updateDate(ProductEntity product);

	public int getNumProductByCategories(int id);

	public boolean checkExistNameUpdate(String id, String name);

	public boolean checkExistName(String name);

	public boolean checkExistId(String id);
	
	public ProductEntity updateProductWithoutCheckAnything (ProductEntity product);
}
