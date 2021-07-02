package com.nashtech.MyBikeShop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nashtech.MyBikeShop.DTO.ProductDTO;
import com.nashtech.MyBikeShop.entity.ProductEntity;
import com.nashtech.MyBikeShop.service.ProductService;

@RestController
@RequestMapping("/api/product")
public class ProductController {
	@Autowired
	private ProductService productService;

	@GetMapping
	public List<ProductEntity> retrieveProducts() {
		return  productService.retrieveProducts();
	}

	@GetMapping("/{id}")
	public ProductEntity findProduct(@PathVariable(name = "id") String id) {
		return productService.getProduct(id);
	}

	@PostMapping
	public ProductEntity saveProduct(@RequestBody ProductDTO newProduct) {
		return productService.createProduct(newProduct);
	}

	@DeleteMapping("/{id}")
	public void deleteProduct(@PathVariable(name = "id") String id) {
		productService.deleteProduct(id);
	}

	@PutMapping
	public void editProduct(@RequestBody ProductDTO product) {
		productService.updateProduct(product);
	}
}
