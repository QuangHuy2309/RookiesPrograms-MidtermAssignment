package com.nashtech.MyBikeShop.Controller;

import java.util.List;
import java.util.Arrays;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.nashtech.MyBikeShop.services.impl.OrderServiceImpl;
import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;


@WebMvcTest(OrderController.class)
public class OrderControllerTest {

//	@Mock
//	private OrderServiceImpl orderService;
//
//	@InjectMocks
//	private OrderController orderController;
//
//	@Test
//	@WithMockUser(roles="ADMIN")
//	public void testCreateOrderTrue() throws Exception {
//		PersonDTO customers = new PersonDTO(1,"lqhuy2309@gmail.com","123456","A","ADMIN");
//		OrderDTO order = new OrderDTO(1, (float) 3.5, "ABC", true,customers);
//		when(orderService.createOrder(Mockito.anyObject())).thenReturn(true);
//		assertEquals(orderController.createOrder(order), "SUCCESS");
////		this.mockMvc.perform(post("/api/order"))
////					.andExpect(status().isOk())
////					.andExpect(content().string(containsString("SUCCESS")));
//					//.andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(order.getId()));
//	}
//	
//	@Test
//	@WithMockUser(roles="ADMIN")
//	public void testCreateOrder() throws Exception {
//		PersonDTO customers = new PersonDTO(1,"lqhuy2309@gmail.com","123456","A","ADMIN");
//		OrderDTO order = new OrderDTO(1, (float) 3.5, "ABC", true,customers);
//		when(orderService.createOrder(Mockito.anyObject())).thenReturn(false);
//		assertEquals(orderController.createOrder(order), "FAILED");
////		this.mockMvc.perform(post("/api/order"))
////					.andExpect(status().isOk())
////					.andExpect(content().string(containsString("SUCCESS")));
//					//.andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(order.getId()));
//	}
	@MockBean
	OrderServiceImpl orderService;
	@MockBean
	UserDetailsServiceImpl userService;
	@MockBean
	JwtUtils jwtUtil;
	@MockBean
	JwtAuthEntryPoint jwtAuthEntryPoint;
	@Autowired
	private MockMvc mockMvc;

//	private WebApplicationContext context;
//
//	ObjectMapper om = new ObjectMapper();

//	@Before
//	private void setUp() {
//		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
//	}
	@Test
	@WithMockUser(roles="ADMIN")
	public void testgetOrder() throws Exception {
		PersonEntity customers = new PersonEntity(1,"lqhuy2309@gmail.com","123456","A","ADMIN");
		OrderEntity order1 = new OrderEntity(1,(float) 3.5, "A", true, customers);
		OrderEntity order2 = new OrderEntity(2,(float) 3.5, "B", true, customers);
		OrderEntity order3 = new OrderEntity(3,(float) 3.5, "C", true, customers);
		List<OrderEntity> listOrder = Arrays.asList(order1,order2,order3);
		Mockito.when(orderService.getOrderPage(Mockito.anyInt(), Mockito.anyInt())).thenReturn(listOrder);
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/order")
				.contentType(MediaType.APPLICATION_JSON)
		        .accept(MediaType.APPLICATION_JSON))
				.andReturn();
		String resultDOW = result.getResponse().getContentAsString();
		System.out.println(resultDOW);
	}
	
	@Test
	public void createOrderTest() throws Exception {
		PersonDTO customers = new PersonDTO(1,"lqhuy2309@gmail.com","123456","A","ADMIN");
		OrderDTO order = new OrderDTO((float) 3.5, "ABC", true,customers);
		OrderEntity orderEntity = new OrderEntity(1,(float) 3.5, "ABC", true, new PersonEntity(customers));
		//orderEntity.setId(0);
//		String jsonRequest = om.writeValueAsString(order);
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		
		Mockito.when(orderService.createOrder(order)).thenReturn(orderEntity);
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/order")
				.content(ow.writeValueAsString(orderEntity))
		        .contentType(MediaType.APPLICATION_JSON)
		        .accept(MediaType.APPLICATION_JSON))
//				.andReturn().getResponse().getContentAsString());
//				.andExpect(MockMvcResultMatchers.status().isOk())
//				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//				.andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)));
		//.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
		.andReturn();
		System.out.println("KET QUA: "+ result.getResponse().getContentAsString());
//			.andExpect(MockMvcResultMatchers.model().attribute("id",1));
	}
}
