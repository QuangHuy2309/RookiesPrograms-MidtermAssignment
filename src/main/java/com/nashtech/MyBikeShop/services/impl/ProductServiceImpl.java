package com.nashtech.MyBikeShop.services.impl;

import java.time.LocalDateTime;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.apache.log4j.Logger;
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

	private static final Logger logger = Logger.getLogger(ProductServiceImpl.class);

	public ProductServiceImpl() {
		super();
	}

	public List<ProductEntity> retrieveProducts() {
		Sort sortable = Sort.by("id").ascending();
		return productRepository.findByStatusNotAndCategoriesStatusNot(sortable, false, false);
	}

	public ProductDTO convertToDTO(ProductEntity product) {
		ProductDTO productDTO = mapper.map(product, ProductDTO.class);
		productDTO.setNameEmployeeUpdate(product.getEmployeeUpdate().getFullname());
		return productDTO;
	}

	public List<ProductEntity> retrieveProductsByType(int categoriesId) {
		Sort sortable = Sort.by("updateDate").descending();
		return productRepository.findByCategoriesIdAndStatusNot(sortable, categoriesId, false);
	}

	public List<ProductEntity> getProductPage(int page, int size, int categoriesId) {
		Sort sortable = Sort.by("updateDate").descending();
		Pageable pageable = PageRequest.of(page, size, sortable);
		return productRepository.findByCategoriesIdAndStatusNot(pageable, categoriesId, false);
	}

	public List<ProductEntity> getNewestProductCategories(int categoriesId, int size) {
		Sort sortable = Sort.by("updateDate").descending().and(Sort.by("quantity").descending());
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
			return productRepository.findByCategoriesIdAndStatusNot(pageable, categoriesId, false);
		}

	}

	public Optional<ProductEntity> getProduct(String id) {
		return productRepository.findByIdIgnoreCaseAndStatusNotAndCategoriesStatusNot(id, false, false);
	}

	public Optional<ProductEntity> getProductInludeDeleted(String id) {
		return productRepository.findByIdIgnoreCase(id);
	}

	public List<ProductEntity> searchProduct(String keyword) {
		return productRepository.searchProduct(keyword.toUpperCase());
	}

	public List<ProductEntity> searchProductByType(String keyword, int type) {
		return productRepository.searchProduct(keyword.toUpperCase(), type);
	}

	public ProductEntity createProduct(ProductDTO productDTO, int id) {
		try {
			boolean checkName = checkExistName(productDTO.getName());
			boolean checkId = checkExistId(productDTO.getId());
			if (!checkId) {
				logger.error(
						"Account id " + id + " create product " + productDTO.getId() + " failed: ID had been used");
				throw new ObjectAlreadyExistException("Error: Product's ID had been used.");
			} else if (!checkName) {
				logger.error(
						"Account id " + id + " create product " + productDTO.getId() + " failed: ID had been used");
				throw new ObjectAlreadyExistException("Error: Product's name had been used=");
			} else {
				CategoriesEntity cate = cateService.getCategories(productDTO.getCategoriesId()).get();
				PersonEntity employee = personService.getPerson(id).get();
				ProductEntity productEntity = new ProductEntity(productDTO, cate);
				productEntity.setCreateDate(LocalDateTime.now());
				productEntity.setUpdateDate(LocalDateTime.now());
				productEntity.setStatus(true);
				productEntity.setEmployeeUpdate(employee);
				logger.info("Account id " + id + " create product id " + productDTO.getId() + " success");
				return productRepository.save(productEntity);
			}
		} catch (DataAccessException ex) {
			logger.error("Account id " + id + " create product " + productDTO.getId() + " failed");
			logger.error(ex.getMessage());
			throw new ObjectNotFoundException("Failed!" + ex.getMessage());
		} catch (IllegalArgumentException ex) {
			logger.error("Account id " + id + " create product " + productDTO.getId() + " failed");
			logger.error(ex.getMessage());
			throw new ObjectPropertiesIllegalException("Failed!" + ex.getMessage());
		} catch (NoSuchElementException ex) {
			logger.error("Account id " + id + " create product " + productDTO.getId()
					+ " failed: No found user with ID " + id);
			throw new ObjectNotFoundException("Error: No found account with ID " + id);
		}
	}

	public boolean deleteProduct(String id, int userId) {
		ProductEntity prod;
		PersonEntity person;
		try {
			prod = getProduct(id).get();
		} catch (NoSuchElementException ex) {
			logger.error(
					"Account id " + userId + " delete product id " + id + " failed: No found product with ID " + id);
			throw new ObjectNotFoundException("Error: No found product with ID " + id);
		}
		try {
			person = personService.getPerson(userId).get();
		} catch (NoSuchElementException ex) {
			logger.error(
					"Account id " + userId + " delete product id " + id + " failed: No found account with ID " + id);
			throw new ObjectNotFoundException("Error: No found account with ID " + id);
		}
//		person.getProduct().remove(prod);
//		productRepository.deleteById(id);
		prod.setUpdateDate(LocalDateTime.now());
		prod.setEmployeeUpdate(person);
		prod.setStatus(false);
		productRepository.save(prod);
		logger.info("Account id " + userId + " delete product id " + id + " success");
		return true;
//		} catch (NoSuchElementException ex) {
//			logger.error("Account id " + userId + " delete product id " + id + " failed: No found account with ID " + id);
//			throw new ObjectNotFoundException("Error: No found account with ID " + id);
//		}
	}

	public boolean updateProduct(ProductDTO productDTO, int userId) {
		try {
			boolean check = checkExistNameUpdate(productDTO.getId(), productDTO.getName());
			if (check) {
				CategoriesEntity cate = cateService.getCategories(productDTO.getCategoriesId()).get();
				PersonEntity employee = personService.getPerson(userId).get();
				ProductEntity product = new ProductEntity(productDTO, cate);
				product.setEmployeeUpdate(employee);
				product.setStatus(true);
				productRepository.save(updateDate(product));
				logger.info("Account id " + userId + " delete product id " + productDTO.getId() + " success");
				return true;
			} else
				logger.error(
						"Account id " + userId + " update product " + productDTO.getId() + " failed: Duplicate name");
			throw new ObjectAlreadyExistException("Error: Product's name had been used");
		} catch (NoSuchElementException ex) {
			logger.error("Account id " + userId + " update product id " + productDTO.getId()
					+ " failed: No found account with ID " + userId);
			throw new ObjectNotFoundException("Error: No found account with ID " + userId);
		}
	}

	public boolean updateProductQuantity(String id, int numberChange) {
		try {
			ProductEntity product = getProduct(id).get();
			if (product.getQuantity() - Math.abs(numberChange) < 0) {
				throw new ObjectPropertiesIllegalException("Quantity of product is not enough to update");
			} else if (!product.isStatus()) {
				logger.error("Update product quantity " + id + " failed: Product not found");
				throw new ObjectNotFoundException("Product not found with id " + id);
			}
			product.changeQuantity(numberChange);
			productRepository.save(updateDate(product));
			return true;
		} catch (NoSuchElementException ex) {
			logger.error("Update product quantity " + id + " failed: Product not found");
			return false;
		}
	}

	public boolean updateProductQuantityToCancel(String id, int numberChange) {
		try {
			ProductEntity product = productRepository.findByIdIgnoreCase(id).get();
			if (product.getQuantity() - Math.abs(numberChange) < 0) {
				throw new ObjectPropertiesIllegalException("Quantity of product is not enough to update");
			}
			product.changeQuantity(numberChange);
			productRepository.save(updateDate(product));
			return true;
		} catch (NoSuchElementException ex) {
			logger.error("Update product quantity " + id + " failed: Product not found");
			ex.printStackTrace();
			return false;
		}
	}

	public List<ProductEntity> findProductByCategories(int id) {
		Sort sortable = Sort.by("updateDate").descending();
		return productRepository.findByCategoriesIdAndStatusNot(sortable, id, false);
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
		Optional<ProductEntity> prod = productRepository.findByIdIgnoreCase(id);
		return prod.isEmpty();
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
