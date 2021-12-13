package com.nashtech.MyBikeShop.Service;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.nashtech.MyBikeShop.DTO.CategoriesDTO;
import com.nashtech.MyBikeShop.entity.CategoriesEntity;
import com.nashtech.MyBikeShop.entity.ProductEntity;
import com.nashtech.MyBikeShop.exception.ObjectAlreadyExistException;
import com.nashtech.MyBikeShop.repository.CategoriesRepository;
import com.nashtech.MyBikeShop.services.CategoriesService;
import com.nashtech.MyBikeShop.services.impl.CategoriesServiceImpl;

@SpringBootTest
public class CategoriesServiceImplTest {
	@Mock
	CategoriesRepository categoriesRepo;
	@InjectMocks
	CategoriesServiceImpl categoriesService;

	@BeforeAll
	public static void setup() {
		System.out.println("Start test Categories");
	}
	CategoriesEntity cate;
	@BeforeEach
	public void beforeEach() {
		//categoriesRepo = Mockito.mock(CategoriesRepository.class);
		//categoriesService = Mockito.mock(CategoriesServiceImpl.class);
		cate = new CategoriesEntity(1, "Cate 1", "This is categories number 1", true);
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
		assertNotNull(categoriesRepo);
		CategoriesEntity cate1 = new CategoriesEntity(1, "Cate 1", "This is categories number 1", true);
		CategoriesEntity cate2 = new CategoriesEntity(1, "Cate 2", "This is categories number 2", true);
		CategoriesEntity cate3 = new CategoriesEntity(1, "Cate 3", "This is categories number 3", true);
		List<CategoriesEntity> listCate = Arrays.asList(cate1, cate2, cate3);
		List<CategoriesEntity> listCate2 = Arrays.asList(cate1, cate2, cate3);
		when(categoriesRepo.findAll()).thenReturn(listCate);
		assertEquals(listCate, listCate2);
	}

	@Test
	public void testCreateCategories_WhenNameExisted() {
		assertNotNull(categoriesRepo);
		assertNotNull(categoriesService);
		
		CategoriesDTO cateDTO = new CategoriesDTO(1, "Cate 1", "This is categories number 1");
		List<CategoriesEntity> list = Arrays.asList(cate);
		when(categoriesRepo.findByNameIgnoreCaseAndStatusNot(cate.getName(),false)).thenReturn(list);
		try {
			assertEquals("There is a category with the same Name",categoriesService.createCategories(cateDTO));
		} catch (ObjectAlreadyExistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void testCreateCategories_WhenNameNOTExisted() {
		assertNotNull(categoriesRepo);
		assertNotNull(categoriesService);
		CategoriesDTO cateDTO = new CategoriesDTO(1, "Cate 1", "This is categories number 1");
		List<CategoriesEntity> emptyList = new ArrayList<CategoriesEntity>(); 
		when(categoriesRepo.findByNameIgnoreCaseAndStatusNot(cateDTO.getName(),false)).thenReturn(emptyList);
		
		try {
			System.out.println(categoriesService.createCategories(cateDTO));
		} catch (ObjectAlreadyExistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			assertTrue(categoriesService.createCategories(cateDTO));
		} catch (ObjectAlreadyExistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testDeleteCategoriesSuccess() {
		when(categoriesRepo.save(cate)).thenReturn(cate);
		assertTrue(categoriesService.deleteCategories(cate));
	}
}
