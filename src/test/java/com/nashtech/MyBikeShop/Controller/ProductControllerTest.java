package com.nashtech.MyBikeShop.Controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.nashtech.MyBikeShop.DTO.ProductDTO;
import com.nashtech.MyBikeShop.controller.ProductController;
import com.nashtech.MyBikeShop.entity.CategoriesEntity;
import com.nashtech.MyBikeShop.entity.ProductEntity;
import com.nashtech.MyBikeShop.services.ProductService;
import com.nashtech.MyBikeShop.services.impl.ProductServiceImpl;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProductService prodService;

	@InjectMocks
	ProductController prodController;

	@Test
	@WithMockUser(username = "admin", password = "123456", roles = "ADMIN") 
	public void testCreateProd() throws Exception{
		CategoriesEntity cate = new CategoriesEntity(1, "cate1", "cateDes");
		ProductEntity prod = new ProductEntity("a1","PRODUCT ENTITY",(float)6.5,4,cate);
		ProductDTO prodDTO = new ProductDTO("a1DTO","PRODUCT DTO",(float)6.5,4,1,"A");
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		
		Mockito.when(prodService.createProduct(Mockito.anyObject(),"A")).thenReturn(prod);
		
		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/product")
				.content(ow.writeValueAsString(prodDTO))
		        .contentType(MediaType.APPLICATION_JSON)
		        .characterEncoding("UTF-8")
		        .accept(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$.id",Matchers.equalTo("a1")))
		.andExpect(MockMvcResultMatchers.jsonPath("$.name",Matchers.equalTo("PRODUCT ENTITY")))
		.andExpect(MockMvcResultMatchers.jsonPath("$.quantity",Matchers.equalTo(4)));
	}
}
