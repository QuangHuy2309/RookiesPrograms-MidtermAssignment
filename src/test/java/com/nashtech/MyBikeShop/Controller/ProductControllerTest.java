package com.nashtech.MyBikeShop.Controller;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.nashtech.MyBikeShop.controller.ProductController;
import com.nashtech.MyBikeShop.services.impl.ProductServiceImpl;

@SpringBootTest
public class ProductControllerTest {
	@Mock
	ProductServiceImpl prodService;
	
	@InjectMocks
	ProductController prodController;
	
	@Test
	public void testGetProd() {
		
	}
}
