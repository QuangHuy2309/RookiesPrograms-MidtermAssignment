package com.nashtech.MyBikeShop.Controller;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.nashtech.MyBikeShop.entity.OrderEntity;
import com.nashtech.MyBikeShop.repository.OrderRepository;
import com.nashtech.MyBikeShop.services.impl.OrderServiceImpl;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class OrderControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private OrderServiceImpl orderService;

	@MockBean
	private OrderRepository orderRepo;

	@Test
	@WithMockUser()
	public void testCreateOrder() {
		OrderEntity order = new OrderEntity(1, (float) 3.5, "ABC", true);
		when(orderRepo.save(order)).thenReturn(order);
		this.mockMvc.perform(get("/users/")).andExpect(status().isOk())
				.andExpect(jsonPath("$.size()", is(userList.size())));
	}
}
