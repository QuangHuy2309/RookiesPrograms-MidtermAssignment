package com.nashtech.MyBikeShop;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.nashtech.MyBikeShop.DTO.CategoriesDTO;
import com.nashtech.MyBikeShop.entity.CategoriesEntity;
import com.nashtech.MyBikeShop.repository.CategoriesRepository;
import com.nashtech.MyBikeShop.services.CategoriesService;

public class CategoriesServiceImplTest {
	CategoriesRepository categoriesRepositoryMock;
	CategoriesService categoriesServiceMock;

	@BeforeAll
	public static void setup() {
		System.out.println("Start test Categories");
	}

	@BeforeEach
	public void beforeEach() {
		categoriesRepositoryMock = Mockito.mock(CategoriesRepository.class);
		categoriesServiceMock = Mockito.mock(CategoriesService.class);
		System.out.println("Before each Testcase");
	}

	@AfterEach
	public void afterEach() {
		System.out.println("After each Testcase");
	}

	@AfterAll
	public static void finishTest() {
		System.out.println("Finish Test");
	}

	@Test
	public void testRetrieveCategories() {
		assertNotNull(categoriesRepositoryMock);
		CategoriesEntity cate1 = new CategoriesEntity(1, "Cate 1", "This is categories number 1");
		CategoriesEntity cate2 = new CategoriesEntity(1, "Cate 2", "This is categories number 2");
		CategoriesEntity cate3 = new CategoriesEntity(1, "Cate 3", "This is categories number 3");
		List<CategoriesEntity> listCate = Arrays.asList(cate1, cate2, cate3);
		List<CategoriesEntity> listCate2 = Arrays.asList(cate1, cate2, cate3);
		when(categoriesRepositoryMock.findAll()).thenReturn(listCate);
		assertEquals(listCate, listCate2);
	}

	@Test
	public void testCreateCategories_NotNull() {
		assertNotNull(categoriesRepositoryMock);
		assertNotNull(categoriesServiceMock);
		CategoriesEntity cateEntity = new CategoriesEntity(1, "Cate 1", "This is categories number 1");
		when(categoriesRepositoryMock.findByName(Mockito.anyString())).thenReturn(cateEntity);
		assertNotNull(categoriesServiceMock.retrieveCategories());

	}

	@Test
	public void testCreateCategories_WhenNameExisted() {
		assertNotNull(categoriesRepositoryMock);
		assertNotNull(categoriesServiceMock);
		CategoriesDTO cateDTO = new CategoriesDTO(1, "Cate 1", "This is categories number 1");
		when(categoriesRepositoryMock.findByName(Mockito.anyString())).thenReturn(null);
		
		System.out.println(categoriesRepositoryMock.findByName(Mockito.anyString()));
		System.out.println(categoriesServiceMock.createCategories(cateDTO));
		
		assertEquals("Success",categoriesServiceMock.createCategories(cateDTO));
	}
}
