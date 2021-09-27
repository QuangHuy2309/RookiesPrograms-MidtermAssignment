package com.nashtech.MyBikeShop.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.nashtech.MyBikeShop.DTO.CategoriesDTO;
import com.nashtech.MyBikeShop.DTO.ProductDTO;
import com.nashtech.MyBikeShop.entity.CategoriesEntity;
import com.nashtech.MyBikeShop.entity.ProductEntity;
import com.nashtech.MyBikeShop.repository.ProductRepository;
import com.nashtech.MyBikeShop.services.impl.CategoriesServiceImpl;
import com.nashtech.MyBikeShop.services.impl.ProductServiceImpl;


@SpringBootTest
public class ProductServiceImplTest {
	
	@Mock
	ProductServiceImpl prodService;
	
	@InjectMocks
	CategoriesServiceImpl cateService;
	
	@Mock
	ProductRepository prodRepository;

	@BeforeAll
	public static void setup() {
		
	}
	
	@Test
	public void testCreateProd() {
		CategoriesEntity cate1 = new CategoriesEntity(1, "Cate 1", "This is categories number 1");
		ProductEntity prod1 = new ProductEntity("ProdA", "Product A", (float) 3.45, 2, cate1);
		ProductDTO prodDTO = new ProductDTO("1","NAME",3,3,1,"A");
		//when(prodRepository.findById(Mockito.anyString())).thenReturn(null);
//		when(cateService.getCategories(Mockito.anyInt())).thenReturn(Mockito.any(Optional.class));
		when(prodRepository.save(Mockito.any())).thenReturn(prod1);
		ProductEntity prod = prodService.createProduct(prodDTO,"A");
//		assertEquals("ProdA",prod.getId() );
	}
	
//	@Test
//	public void testUpdateProd() {
//		CategoriesEntity cate1 = new CategoriesEntity(1, "Cate 1", "This is categories number 1");
//		ProductEntity prod1 = new ProductEntity("ProdA", "Product A", (float) 3.45, 2, cate1);
//		ProductDTO prodDTO = new ProductDTO("1","NAME",3,3,1);
//		//when(prodRepository.findById(Mockito.anyString())).thenReturn(null);
//		when(prodRepository.save(Mockito.any(ProductEntity.class))).thenReturn(prod1);
//		assertEquals(prod1, prodService.updateProduct(prodDTO));
//	}
}
