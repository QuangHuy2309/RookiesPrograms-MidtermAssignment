package com.nashtech.MyBikeShop.Service;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;

import com.nashtech.MyBikeShop.DTO.CategoriesDTO;
import com.nashtech.MyBikeShop.DTO.ProductDTO;
import com.nashtech.MyBikeShop.entity.CategoriesEntity;
import com.nashtech.MyBikeShop.entity.PersonEntity;
import com.nashtech.MyBikeShop.entity.ProductEntity;
import com.nashtech.MyBikeShop.repository.ProductRepository;
import com.nashtech.MyBikeShop.services.CategoriesService;
import com.nashtech.MyBikeShop.services.PersonService;
import com.nashtech.MyBikeShop.services.ProductService;
import com.nashtech.MyBikeShop.services.impl.CategoriesServiceImpl;
import com.nashtech.MyBikeShop.services.impl.ProductServiceImpl;


@SpringBootTest
public class ProductServiceImplTest {
	
	@InjectMocks
	ProductService prodService = new ProductServiceImpl();
	
	@Mock
	CategoriesService cateService;
	
	@Mock
	PersonService personService;
	
	@Mock
	ProductRepository prodRepository;
	
	private CategoriesEntity cate1;
	private ProductEntity prod1;
	private ProductEntity prod2;
	private ProductEntity prod3;
	private ProductDTO prodDTO;
	private PersonEntity person1;
	List<ProductEntity> prodList;
	private final int list_size = 3;
	private final int cate_id = 1;
	
	@BeforeEach
	public void setup() {
		cate1 = new CategoriesEntity(1, "Cate 1", "This is categories number 1",true);
		prod1 = new ProductEntity("ProdA", "Product A", (float) 3.45, 2, cate1);
		prod2 = new ProductEntity("ProdB", "Product B", (float) 3.45, 2, cate1);
		prod3 = new ProductEntity("ProdC", "Product C", (float) 3.45, 2, cate1);
		prodList = new ArrayList<ProductEntity>(List.of(prod1,prod2,prod3));
		prodDTO = new ProductDTO("ProdA","Product A",(float) 3.45,3,1,"lqhuy2309@gmail.com");
		person1 = new PersonEntity(1,"lqhuy2309@gmail.com","123456","Quang Huy","ADMIN");
	}
	
	@Test
	public void retrieveProductsSuccess_Test() {
		Sort sortable = Sort.by("id").ascending();
		when(prodRepository.findByStatusNotAndCategoriesStatusNot(sortable, false, false)).thenReturn(prodList);
		List<ProductEntity> prodList_test = prodService.retrieveProducts();
		assertEquals(list_size, prodList_test.size());
	}
	@Test
	public void retrieveProductsByTypeSuccess_Test() {
		when(prodRepository.findByCategoriesIdAndStatusNot(1, false)).thenReturn(prodList);
		List<ProductEntity> prodList_test = prodService.retrieveProductsByType(1);
		assertEquals(list_size, prodList_test.size());
	}
	
	@Test
	public void createProdSuccess_Test() {
		when(cateService.getCategories(prodDTO.getCategoriesId())).thenReturn(Optional.of(cate1));
		when(personService.getPerson(Mockito.anyString())).thenReturn(person1);
		when(prodRepository.save(Mockito.any(ProductEntity.class))).thenReturn(prod1);
		ProductEntity prod_test = prodService.createProduct(prodDTO,"lqhuy2309@gmail.com");
		assertEquals(prod1, prod_test);
	}
	
	@Test
	public void updateProdSuccess_Test() {
		List<ProductEntity> EmptyList = new ArrayList<ProductEntity>();  
		when(cateService.getCategories(prodDTO.getCategoriesId())).thenReturn(Optional.of(cate1));
		when(personService.getPerson(Mockito.anyString())).thenReturn(person1);
		when(prodRepository.findByNameIgnoreCaseAndStatusNot(prodDTO.getName(), false)).thenReturn(EmptyList);
		when(prodRepository.save(Mockito.any(ProductEntity.class))).thenReturn(prod1);
		assertTrue(prodService.updateProduct(prodDTO,"admin1@gmail.com"));
	}
	
	@Test
	public void deleteProductSuccess_Test() {
		when(prodRepository.save(Mockito.any(ProductEntity.class))).thenReturn(prod1);
		when(prodRepository.findById(Mockito.anyString())).thenReturn(Optional.of(prod1));
		assertTrue(prodService.deleteProduct(Mockito.anyString()));
	}
}
