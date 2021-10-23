package com.nashtech.MyBikeShop.Service;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import com.nashtech.MyBikeShop.services.OrderImportDetailService;
import com.nashtech.MyBikeShop.services.OrderImportService;
import com.nashtech.MyBikeShop.services.PersonService;
import com.nashtech.MyBikeShop.services.ProductService;
import com.nashtech.MyBikeShop.services.impl.OrderImportDetailServiceImpl;

@SpringBootTest
public class OrderImportDetailServiceImplTest {
	@InjectMocks
	OrderImportDetailService detailImportService = new OrderImportDetailServiceImpl();
	
	@Mock
	ProductService productService;

	@Mock
	ModelMapper mapper;

	@Mock
	PersonService personService;
	
	@Mock
	OrderImportService importService;
}
