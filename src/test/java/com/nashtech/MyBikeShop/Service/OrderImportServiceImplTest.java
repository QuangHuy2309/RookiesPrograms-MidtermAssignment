package com.nashtech.MyBikeShop.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.*;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import com.nashtech.MyBikeShop.DTO.OrderImportDTO;
import com.nashtech.MyBikeShop.entity.CategoriesEntity;
import com.nashtech.MyBikeShop.entity.OrderEntity;
import com.nashtech.MyBikeShop.entity.OrderImportDetailEntity;
import com.nashtech.MyBikeShop.entity.OrderImportDetailEntity.OrderImportDetailsKey;
import com.nashtech.MyBikeShop.repository.OrderImportRepository;
import com.nashtech.MyBikeShop.entity.OrderImportEntity;
import com.nashtech.MyBikeShop.entity.PersonEntity;
import com.nashtech.MyBikeShop.entity.ProductEntity;
import com.nashtech.MyBikeShop.services.OrderImportDetailService;
import com.nashtech.MyBikeShop.services.OrderImportService;
import com.nashtech.MyBikeShop.services.PersonService;
import com.nashtech.MyBikeShop.services.ProductService;
import com.nashtech.MyBikeShop.services.impl.OrderImportServiceImpl;

@SpringBootTest
public class OrderImportServiceImplTest {
	@InjectMocks
	OrderImportService importService = new OrderImportServiceImpl();

	@Mock
	ProductService productService;

	@Mock
	ModelMapper mapper;

	@Mock
	PersonService personService;

	@Mock
	OrderImportDetailService importDetailService;

	@Mock
	OrderImportRepository orderImportRepo;

	private OrderImportEntity import1;
	private OrderImportEntity import2;
	private OrderImportEntity import3;
	private OrderImportDTO import1DTO;
	private OrderImportDetailEntity detail1;
	private OrderImportDetailEntity detail2;
	private OrderImportDetailEntity detail3;
	private PersonEntity person1;
	private ProductEntity prod1;
	private List<OrderImportEntity> listImport;
	private Set<OrderImportDetailEntity> listDetail;

	@BeforeEach
	public void setup() {
		detail1 = new OrderImportDetailEntity(new OrderImportDetailsKey(1, "ProdA"), 1, (float) 1.0);
		detail2 = new OrderImportDetailEntity(new OrderImportDetailsKey(1, "ProdB"), 2, (float) 2.0);
		detail3 = new OrderImportDetailEntity(new OrderImportDetailsKey(1, "ProdC"), 3, (float) 3.0);
		person1 = new PersonEntity(1, "lqhuy2309@gmail.com", "123456", "Quang Huy", "ADMIN");
		prod1 = new ProductEntity("ProdA", "Product A", (float) 3.45, 2,
				new CategoriesEntity(1, "Cate 1", "This is categories number 1", true));

		listDetail = Stream.of(detail1, detail2, detail3).collect(Collectors.toCollection(HashSet::new));
		import1 = new OrderImportEntity(1, true, person1, listDetail);
		import2 = new OrderImportEntity(2, true, person1, listDetail);
		import3 = new OrderImportEntity(3, true, person1, listDetail);
		import1DTO = new OrderImportDTO();
		listImport = new ArrayList<OrderImportEntity>(List.of(import1, import2, import3));
		person1.setOrdersImport(new HashSet<OrderImportEntity>(listImport));
	}

	@Test
	public void findOrderImportByIdSuccess_Test() {
		when(orderImportRepo.findById(import1.getId())).thenReturn(Optional.of(import1));
		assertEquals(import1, importService.findOrderImportById(import1.getId()));
	}

	@Test
	public void createOrderImportSuccess_Test() {
		when(productService.getProduct(Mockito.anyString())).thenReturn(Optional.of(prod1));
		when(productService.updateProductWithoutCheckAnything(prod1)).thenReturn(prod1);
		when(orderImportRepo.save(import1)).thenReturn(import1);
		assertEquals(import1, importService.createOrderImport(import1));
	}

	@Test
	public void updateOrderImportSuccess_Test() {
		when(productService.getProduct(Mockito.anyString())).thenReturn(Optional.of(prod1));
		when(productService.updateProductWithoutCheckAnything(prod1)).thenReturn(prod1);
		when(orderImportRepo.findById(import1.getId())).thenReturn(Optional.of(import1));
		when(orderImportRepo.save(import1)).thenReturn(import1);
		assertEquals(import1, importService.updateOrderImport(import1DTO, import1.getId()));
	}

	@Test
	public void deleteOrderImportSuccess_Test() {
		when(productService.updateProductWithoutCheckAnything(prod1)).thenReturn(prod1);
		when(personService.getPerson(Mockito.anyInt())).thenReturn(Optional.of(person1));
		when(orderImportRepo.findById(import1.getId())).thenReturn(Optional.of(import1));
		when(orderImportRepo.save(import1)).thenReturn(import1);

		assertTrue(importService.deleteOrderImport(import1.getId()));
	}
}
