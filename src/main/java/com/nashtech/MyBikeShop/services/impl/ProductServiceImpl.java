package com.nashtech.MyBikeShop.services.impl;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nashtech.MyBikeShop.DTO.ProductDTO;
import com.nashtech.MyBikeShop.entity.CategoriesEntity;
import com.nashtech.MyBikeShop.entity.OrderEntity;
import com.nashtech.MyBikeShop.entity.PersonEntity;
import com.nashtech.MyBikeShop.entity.ProductEntity;
import com.nashtech.MyBikeShop.exception.ObjectAlreadyExistException;
import com.nashtech.MyBikeShop.exception.ObjectNotFoundException;
import com.nashtech.MyBikeShop.exception.ObjectPropertiesIllegalException;
import com.nashtech.MyBikeShop.exception.ObjectViolateForeignKeyException;
import com.nashtech.MyBikeShop.repository.ProductRepository;
import com.nashtech.MyBikeShop.services.CategoriesService;
import com.nashtech.MyBikeShop.services.OrderService;
import com.nashtech.MyBikeShop.services.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	ProductRepository productRepository;

	@Autowired
	OrderService orderService;

	@Autowired
	CategoriesService cateService;

	public ProductServiceImpl(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public List<ProductEntity> retrieveProducts() {
		return productRepository.findAll();
	}

	public List<ProductEntity> getProductPage(int page, int size, int categoriesId) {
		Sort sortable = Sort.by("updateDate").descending();
		Pageable pageable = PageRequest.of(page, size, sortable);
		return productRepository.findByCategoriesId(pageable, categoriesId);
	}

	public List<ProductEntity> getNewestProductCategories(int categoriesId, int size) {
		Sort sortable = Sort.by("updateDate").descending();
		Pageable pageable = PageRequest.of(0, size, sortable);
		return productRepository.findByCategoriesId(pageable, categoriesId);
	}

	public Optional<ProductEntity> getProduct(String id) {
		return productRepository.findById(id);
	}

	public ProductEntity createProduct(ProductDTO productDTO) {
		try {
			boolean checkName = existName(productDTO.getName());
			boolean checkId = existId(productDTO.getId());
			if (!checkId) {
				throw new ObjectAlreadyExistException(
						"Failed! There is a product with this id. Please change product id");
			} else if (!checkName) {
				throw new ObjectAlreadyExistException(
						"Failed! There is a product with this name. Please change product name");
			} else {
				CategoriesEntity cate = cateService.getCategories(productDTO.getCategoriesId()).get();
				ProductEntity productEntity = new ProductEntity(productDTO, cate);
				productEntity.setCreateDate(LocalDateTime.now());
				productEntity.setUpdateDate(LocalDateTime.now());
				return productRepository.save(productEntity);
			}
		} catch (DataAccessException ex) {
			throw new ObjectNotFoundException("Failed!" + ex.getMessage());
		} catch (IllegalArgumentException ex) {
			throw new ObjectPropertiesIllegalException("Failed!" + ex.getMessage());
		}
	}

	public boolean deleteProduct(String id) {
		productRepository.deleteById(id);
		return true;
	}

	public boolean updateProduct(ProductDTO productDTO) {
		boolean check = existNameUpdate(productDTO.getId(), productDTO.getName());
		if (check) {
			CategoriesEntity cate = cateService.getCategories(productDTO.getCategoriesId()).get();
			ProductEntity product = new ProductEntity(productDTO, cate);
			productRepository.save(updateDate(product));
			return true;
		} else
			throw new ObjectAlreadyExistException("There is a product with the same Name");
	}

	public boolean updateProductQuantity(String id, int numberChange) {
		try {
			ProductEntity product = getProduct(id).get();
			product.changeQuantity(numberChange);
			// productRepository.save(updateDate(product));
			return true;
		} catch (NoSuchElementException ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public List<ProductEntity> findProductByCategories(int id) {
		return productRepository.findByCategoriesId(id);
	}

	public ProductEntity updateDate(ProductEntity product) {
		product.setUpdateDate(LocalDateTime.now());
		return product;
	}

	public int getNumProductByCategories(int id) {
		return productRepository.countByCategoriesId(id);
	}

	public boolean storeImage(MultipartFile file, String prodId) throws IOException {
//		byte[] fileContent = FileUtils.readFileToByteArray((File) file);
//		ProductEntity prod = productRepository.getById(prodId);
//		String encodedString = Base64.getEncoder().encodeToString(fileContent);
//		prod.setPhoto(encodedString);
//		productRepository.save(prod);
		return true;

	}

	public boolean existNameUpdate(String id, String name) {
		List<ProductEntity> prodList = productRepository.findByNameIgnoreCase(name);
		if (prodList.isEmpty())
			return true;
		else if ((prodList.size() > 1) || ((prodList.size() == 1) && (!prodList.get(0).getId().equalsIgnoreCase(id))))
			return false;
		else
			return true;
	}

	public boolean existId(String id) {
		List<ProductEntity> prodList = productRepository.findByIdIgnoreCase(id);
		return prodList.isEmpty();
	}

	public boolean existName(String name) {
		List<ProductEntity> prodList = productRepository.findByNameIgnoreCase(name);
		return prodList.isEmpty();
	}
//	public MultipartFile convertToImg(String encodedString) throws IOException {
//		Date date = Calendar.getInstance().getTime();
//		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
//		String strName = dateFormat.format(date);
//		byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
//
//		FileUtils.writeByteArrayToFile(new File(strName), decodedBytes);
//	}
}
