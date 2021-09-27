package com.nashtech.MyBikeShop.Controller;

import java.util.List;
import java.util.Arrays;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.nashtech.MyBikeShop.DTO.OrderDTO;
import com.nashtech.MyBikeShop.DTO.PersonDTO;
import com.nashtech.MyBikeShop.controller.OrderController;
import com.nashtech.MyBikeShop.entity.OrderEntity;
import com.nashtech.MyBikeShop.entity.PersonEntity;
import com.nashtech.MyBikeShop.security.JWT.JwtAuthEntryPoint;
import com.nashtech.MyBikeShop.security.JWT.JwtUtils;
import com.nashtech.MyBikeShop.security.services.UserDetailsServiceImpl;
import com.nashtech.MyBikeShop.services.OrderService;
import com.nashtech.MyBikeShop.services.impl.OrderServiceImpl;
import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;


//@WebMvcTest(OrderController.class)
@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private OrderService orderService;

	@InjectMocks
	private OrderController orderController;

	@Test
	@WithMockUser(username = "admin", password = "123456", roles = "ADMIN") 
	public void testGetOrder() throws Exception {
		PersonEntity customers = new PersonEntity(1,"lqhuy2309@gmail.com","123456","A","ADMIN");
		OrderEntity order1 = new OrderEntity(1,  "A", 1, customers);
		OrderEntity order2 = new OrderEntity(2, "B", 1, customers);
		OrderEntity order3 = new OrderEntity(3,  "C", 1, customers);
		List<OrderEntity> listOrder = Arrays.asList(order1,order2,order3);
		
		Mockito.when(orderService.getOrderPage(Mockito.anyInt(), Mockito.anyInt()))
											.thenReturn(listOrder);
				mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/order?pagenum=4&size=4"))
				//.contentType(MediaType.APPLICATION_JSON)
				//.characterEncoding("UTF-8")
		        //.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(3)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)));
//				.andReturn();
//		String resultDOW = result.getResponse().getContentAsString();
//		System.out.println("KET QUA: " +resultDOW);
	}
	
	@Test
	@WithMockUser(roles = "USER") 
	public void createOrderTest() throws Exception {
		PersonDTO customers = new PersonDTO(1,"lqhuy2309@gmail.com","123456","A","ADMIN", true);
		OrderDTO orderDTO = new OrderDTO(  "ABC", 1,"lqhuy2309@gmail.com");
		OrderEntity order = new OrderEntity(1,  "ABC", 1, new PersonEntity(customers));
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		
		Mockito.when(orderService.createOrder(Mockito.anyObject())).thenReturn(order);
		
		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/order")
				.content(ow.writeValueAsString(orderDTO))
		        .contentType(MediaType.APPLICATION_JSON)
		        .characterEncoding("UTF-8")
		        .accept(MediaType.APPLICATION_JSON))
//				.andReturn().getResponse().getContentAsString());
				.andExpect(MockMvcResultMatchers.status().isOk())
//				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//				.andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)));
		.andExpect(MockMvcResultMatchers.jsonPath("$.id",Matchers.equalTo(1)));
//		.andReturn();
//		System.out.println("KET QUA: "+ result.getResponse().getContentAsString());
//			.andExpect(MockMvcResultMatchers.model().attribute("id",1));
	}
}
