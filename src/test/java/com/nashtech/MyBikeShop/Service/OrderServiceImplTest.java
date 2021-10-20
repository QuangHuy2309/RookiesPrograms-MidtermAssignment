package com.nashtech.MyBikeShop.Service;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.nashtech.MyBikeShop.DTO.OrderDTO;
import com.nashtech.MyBikeShop.DTO.OrderDetailDTO;
import com.nashtech.MyBikeShop.entity.CategoriesEntity;
import com.nashtech.MyBikeShop.entity.OrderDetailEntity;
import com.nashtech.MyBikeShop.entity.OrderEntity;
import com.nashtech.MyBikeShop.entity.PersonEntity;
import com.nashtech.MyBikeShop.entity.ProductEntity;
import com.nashtech.MyBikeShop.exception.ObjectPropertiesIllegalException;
import com.nashtech.MyBikeShop.repository.OrderRepository;
import com.nashtech.MyBikeShop.services.OrderDetailService;
import com.nashtech.MyBikeShop.services.OrderService;
import com.nashtech.MyBikeShop.services.PersonService;
import com.nashtech.MyBikeShop.services.ProductService;
import com.nashtech.MyBikeShop.services.impl.OrderServiceImpl;

@SpringBootTest
public class OrderServiceImplTest {
	@InjectMocks
	OrderService orderService = new OrderServiceImpl();

	@Mock
	OrderRepository orderRepository;

	@Mock
	ProductService productService;

	@Mock
	ModelMapper mapper;

	@Mock
	PersonService personService;

	@Mock
	OrderDetailService orderDetailService;

	private OrderEntity order1;
	private OrderEntity order2;
	private OrderEntity order3;
	private OrderEntity order4;
	private OrderDetailEntity detail1;
	private OrderDetailDTO detail1DTO;
	private OrderDetailDTO detail2DTO;
	private OrderDTO order1DTO;
	private PersonEntity person1;
	private CategoriesEntity cate1;
	private ProductEntity prod1;
	List<OrderEntity> listOrder;
	List<OrderDetailDTO> listDetailDTO;
	private final int list_size = 4;

	@BeforeEach
	public void setup() {
		person1 = new PersonEntity(1, "lqhuy2309@gmail.com", "123456", "Quang Huy", "ADMIN");
		order1 = new OrderEntity(1, "Quan 1", 1, person1);
		order2 = new OrderEntity(2, "Quan 2", 2, person1);
		order3 = new OrderEntity(3, "Quan 3", 3, person1);
		order4 = new OrderEntity(4, "Quan 4", 4, person1);
		detail1DTO = new OrderDetailDTO(1, "ProdA", 11, 1000.0);
		detail2DTO = new OrderDetailDTO(2, "ProdB", 22, 2000.0);

		cate1 = new CategoriesEntity(1, "Cate 1", "This is categories number 1", true);
		prod1 = new ProductEntity("ProdA", "Product A", (float) 3.45, 2, cate1);
		listDetailDTO = new ArrayList<OrderDetailDTO>(List.of(detail1DTO, detail2DTO));
		listOrder = new ArrayList<OrderEntity>(List.of(order1, order2, order3, order4));
		order1DTO = new OrderDTO(1, "Quan 1", 1, person1.getEmail(), listDetailDTO);
		person1.setOrders(new HashSet<OrderEntity>(listOrder));
	}

	@Test
	public void retrieveOrdersSuccess_Test() {
		when(orderRepository.findAll()).thenReturn(listOrder);
		assertEquals(list_size, orderService.retrieveOrders().size());
	}

	@Test
	public void createOrderSuccess_Test() throws MessagingException {
		when(personService.getPerson(Mockito.anyString())).thenReturn(person1);
		when(orderRepository.save(Mockito.any(OrderEntity.class))).thenReturn(order1);
		when(orderDetailService.createDetail(Mockito.any(OrderDetailEntity.class))).thenReturn(true);
		when(productService.getProduct(Mockito.anyString())).thenReturn(Optional.of(prod1));
		when(orderRepository.getById(Mockito.anyInt())).thenReturn(order1);
		JavaMailSender javaMailSender = spy(new JavaMailSenderImpl());
		MimeMessage mimeMessage = mock(MimeMessage.class);
		OrderService orderService_test = new OrderServiceImpl(orderRepository, productService, mapper, personService,
				orderDetailService, javaMailSender);
		doReturn(mimeMessage).when(javaMailSender).createMimeMessage();
		doNothing().when(javaMailSender).send(Mockito.any(MimeMessage.class));
		OrderEntity order_test = orderService_test.createOrder(order1DTO);
		assertEquals(order1DTO.getId(), order_test.getId());
	}

	@Test
	public void createOrderFailedAddDetail_Test() {
		when(personService.getPerson(Mockito.anyString())).thenReturn(person1);
		when(orderRepository.save(Mockito.any(OrderEntity.class))).thenReturn(order1);
		when(orderDetailService.createDetail(Mockito.any(OrderDetailEntity.class))).thenReturn(false);
		when(productService.getProduct(Mockito.anyString())).thenReturn(Optional.of(prod1));
		when(orderRepository.getById(Mockito.anyInt())).thenReturn(order1);
		assertThrows(ObjectPropertiesIllegalException.class, () -> orderService.createOrder(order1DTO));
	}

	@Test
	public void updateOrderSuccess_Test() {
		when(orderRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(order1));
		when(orderDetailService.getDetailOrderByOrderId(Mockito.anyInt())).thenReturn(new HashSet<OrderDetailEntity>() {
			{
				add(detail1);
			}
		});
		when(orderDetailService.deleteDetail(detail1)).thenReturn(true);
		when(orderDetailService.createDetail(Mockito.any(OrderDetailEntity.class))).thenReturn(true);
		when(orderRepository.save(Mockito.any(OrderEntity.class))).thenReturn(order1);
		assertTrue(orderService.updateOrder(order1DTO));
	}

	@Test
	public void updateOrderFalseDeleteDetail_Test() {
		when(orderRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(order1));
		when(orderDetailService.getDetailOrderByOrderId(Mockito.anyInt())).thenReturn(new HashSet<OrderDetailEntity>() {
			{
				add(detail1);
			}
		});
		when(orderDetailService.deleteDetail(Mockito.any(OrderDetailEntity.class))).thenReturn(false);
		when(orderDetailService.createDetail(Mockito.any(OrderDetailEntity.class))).thenReturn(true);
		when(orderRepository.save(Mockito.any(OrderEntity.class))).thenReturn(order1);
		assertFalse(orderService.updateOrder(order1DTO));
	}

	@Test
	public void deleteOrderSuccess_Test() {
		when(orderRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(order1));
		when(personService.getPerson(Mockito.anyInt())).thenReturn(Optional.of(person1));
		when(productService.updateProductQuantity(Mockito.anyString(), Mockito.anyInt())).thenReturn(true);
		doNothing().when(orderRepository).delete(Mockito.any(OrderEntity.class));
		assertTrue(orderService.deleteOrder(order1.getId()));
		assertFalse(person1.getOrders().contains(order1));

	}
}
