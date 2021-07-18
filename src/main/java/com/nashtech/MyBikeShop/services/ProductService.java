package com.nashtech.MyBikeShop.services;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.nashtech.MyBikeShop.DTO.ProductDTO;
import com.nashtech.MyBikeShop.entity.ProductEntity;


public interface ProductService {
	public List<ProductEntity> retrieveProducts();
	public List<ProductEntity> getProductPage(int num, int size, int categoriesId);
	public List<ProductEntity> getNewestProductCategories(int idCategrories, int size);
	public Optional<ProductEntity> getProduct(String id);
	public ProductEntity createProduct(ProductDTO product);
	public boolean deleteProduct(String id);
	public boolean updateProduct(ProductDTO product);
	public boolean updateProductQuantity(String id, int numberChange);
	public List<ProductEntity> findProductByCategories(int id);
	public ProductEntity updateDate(ProductEntity product);
	public boolean storeImage(MultipartFile file, String id) throws IOException;
	public int getNumProductByCategories(int id);
//	public MultipartFile convertToImg(String encodedString) throws IOException;
}
