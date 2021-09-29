package com.nashtech.MyBikeShop.services.impl;

import java.time.LocalDateTime;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.nashtech.MyBikeShop.DTO.ProductDTO;
import com.nashtech.MyBikeShop.entity.CategoriesEntity;
import com.nashtech.MyBikeShop.entity.PersonEntity;
import com.nashtech.MyBikeShop.entity.ProductEntity;
import com.nashtech.MyBikeShop.exception.ObjectAlreadyExistException;
import com.nashtech.MyBikeShop.exception.ObjectNotFoundException;
import com.nashtech.MyBikeShop.exception.ObjectPropertiesIllegalException;
import com.nashtech.MyBikeShop.repository.ProductRepository;
import com.nashtech.MyBikeShop.services.CategoriesService;
import com.nashtech.MyBikeShop.services.OrderService;
import com.nashtech.MyBikeShop.services.PersonService;
import com.nashtech.MyBikeShop.services.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	ProductRepository productRepository;

	@Autowired
	ModelMapper mapper;
	
	@Autowired
	OrderService orderService;

	@Autowired
	CategoriesService cateService;
	
	@Autowired
	PersonService personService;

	public ProductServiceImpl(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public List<ProductEntity> retrieveProducts() {
		return productRepository.findAll();
	}

	public ProductDTO convertToDTO(ProductEntity product) {
		ProductDTO productDTO = mapper.map(product, ProductDTO.class);
		productDTO.setNameEmployeeUpdate(product.getEmployeeUpdate().getFullname());
		return productDTO;
	}
	public List<ProductEntity> retrieveProductsByType(int categoriesId){
		return productRepository.findByCategoriesIdAndStatusNot(categoriesId,false);
	}

	public List<ProductEntity> getProductPage(int page, int size, int categoriesId) {
		Sort sortable = Sort.by("updateDate").descending();
		Pageable pageable = PageRequest.of(page, size, sortable);
		return productRepository.findByCategoriesIdAndStatusNot(pageable, categoriesId, false);
	}

	public List<ProductEntity> getNewestProductCategories(int categoriesId, int size) {
		Sort sortable = Sort.by("updateDate").descending();
		Pageable pageable = PageRequest.of(0, size, sortable);
		return productRepository.findByCategoriesIdAndStatusNot(pageable, categoriesId, false);
	}

	public List<ProductEntity> getProductPageWithSort(int page, int size, int categoriesId, String sortType) {

		if (sortType.equalsIgnoreCase("ASC")) {
			Sort sortable = Sort.by("price").ascending();
			Pageable pageable = PageRequest.of(page, size, sortable);
			return productRepository.findByCategoriesIdAndStatusNot(pageable, categoriesId, false);
		} else {
			Sort sortable = Sort.by("price").descending();
			Pageable pageable = PageRequest.of(page, size, sortable);
			return productRepository.findByCategoriesIdAndStatusNot(pageable, categoriesId,false);
		}

	}

	public Optional<ProductEntity> getProduct(String id) {
		return productRepository.findById(id);
	}
	
	public List<ProductEntity> searchProduct(String keyword){
		return productRepository.searchProduct(keyword.toUpperCase());
	}
	
	public List<ProductEntity> searchProductByType(String keyword, int type){
		return productRepository.searchProduct(keyword.toUpperCase(), type);
	}
	
	public ProductEntity createProduct(ProductDTO productDTO, String email) {
		try {
			boolean checkName = checkExistName(productDTO.getName());
			boolean checkId = checkExistId(productDTO.getId());
			if (!checkId) {
				throw new ObjectAlreadyExistException(
						"Failed! There is a product with this id. Please change product id");
			} else if (!checkName) {
				throw new ObjectAlreadyExistException(
						"Failed! There is a product with this name. Please change product name");
			} else {
				CategoriesEntity cate = cateService.getCategories(productDTO.getCategoriesId()).get();
				PersonEntity employee = personService.getPerson(email);
				ProductEntity productEntity = new ProductEntity(productDTO, cate);
				productEntity.setCreateDate(LocalDateTime.now());
				productEntity.setUpdateDate(LocalDateTime.now());
				productEntity.setStatus(true);
				productEntity.setEmployeeUpdate(employee);
				return productRepository.save(productEntity);
			}
		} catch (DataAccessException ex) {
			throw new ObjectNotFoundException("Failed!" + ex.getMessage());
		} catch (IllegalArgumentException ex) {
			throw new ObjectPropertiesIllegalException("Failed!" + ex.getMessage());
		}
	}

	public boolean deleteProduct(String id) {
		ProductEntity prod = getProduct(id).get();
//		PersonEntity person = personService.getPerson(prod.getEmployeeUpdate().getId()).get();
//		person.getProduct().remove(prod);
//		productRepository.deleteById(id);
		prod.setStatus(false);
		productRepository.save(prod);
		return true;
	}

	public boolean updateProduct(ProductDTO productDTO, String email) {
		boolean check = checkExistNameUpdate(productDTO.getId(), productDTO.getName());
		if (check) {
			CategoriesEntity cate = cateService.getCategories(productDTO.getCategoriesId()).get();
			PersonEntity employee = personService.getPerson(email);
			ProductEntity product = new ProductEntity(productDTO, cate);
			product.setEmployeeUpdate(employee);
			product.setStatus(true);
			productRepository.save(updateDate(product));
			return true;
		} else
			throw new ObjectAlreadyExistException("There is a product with the same Name");
	}

	public boolean updateProductQuantity(String id, int numberChange) {
		try {
			ProductEntity product = getProduct(id).get();
			if (product.getQuantity() - numberChange < 0) {
				throw new ObjectPropertiesIllegalException("Quantity of Product is not enought");
			}
			else if (!product.isStatus()) {
				throw new ObjectNotFoundException("Product not found with id "+id);
			}
			product.changeQuantity(numberChange);
			// productRepository.save(updateDate(product));
			return true;
		} catch (NoSuchElementException ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public List<ProductEntity> findProductByCategories(int id) {
		return productRepository.findByCategoriesIdAndStatusNot(id,false);
	}

	public ProductEntity updateDate(ProductEntity product) {
		product.setUpdateDate(LocalDateTime.now());
		return product;
	}

	public int getNumProductByCategories(int id) {
		return productRepository.countByCategoriesIdAndStatusNot(id, false);
	}

	public boolean checkExistNameUpdate(String id, String name) {
		List<ProductEntity> prodList = productRepository.findByNameIgnoreCaseAndStatusNot(name, false);
		if (prodList.isEmpty())
			return true;
		else if ((prodList.size() > 1) || ((prodList.size() == 1) && (!prodList.get(0).getId().equalsIgnoreCase(id))))
			return false;
		else
			return true;
	}

	public boolean checkExistId(String id) {
		List<ProductEntity> prodList = productRepository.findByIdIgnoreCaseAndStatusNot(id, false);
		return prodList.isEmpty();
	}

	public boolean checkExistName(String name) {
		List<ProductEntity> prodList = productRepository.findByNameIgnoreCaseAndStatusNot(name, false);
		return prodList.isEmpty();
	}

	@Override
	public ProductEntity updateProductWithoutCheckAnything(ProductEntity product) {
		return productRepository.save(product);
	}
}
