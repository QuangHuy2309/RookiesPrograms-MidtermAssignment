package com.nashtech.MyBikeShop.services.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParseException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.nashtech.MyBikeShop.DTO.OrderDTO;
import com.nashtech.MyBikeShop.DTO.OrderDetailDTO;
import com.nashtech.MyBikeShop.entity.OrderDetailEntity;
import com.nashtech.MyBikeShop.entity.OrderEntity;
import com.nashtech.MyBikeShop.entity.PersonEntity;
import com.nashtech.MyBikeShop.entity.ProductEntity;
import com.nashtech.MyBikeShop.entity.OrderDetailEntity.OrderDetailsKey;
import com.nashtech.MyBikeShop.exception.JsonGetDataException;
import com.nashtech.MyBikeShop.exception.ObjectAlreadyExistException;
import com.nashtech.MyBikeShop.exception.ObjectNotFoundException;
import com.nashtech.MyBikeShop.exception.ObjectPropertiesIllegalException;
import com.nashtech.MyBikeShop.repository.OrderRepository;
import com.nashtech.MyBikeShop.services.OrderDetailService;
import com.nashtech.MyBikeShop.services.OrderService;
import com.nashtech.MyBikeShop.services.PersonService;
import com.nashtech.MyBikeShop.services.ProductService;

@Service
public class OrderServiceImpl implements OrderService {
	@Autowired
	OrderRepository orderRepository;

	@Autowired
	ProductService productService;

	@Autowired
	PersonService personService;

	@Autowired
	OrderDetailService orderDetailService;

	public OrderServiceImpl(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	public List<OrderEntity> retrieveOrders() {
		return orderRepository.findAll();

	}

	public Optional<OrderEntity> getOrders(int id) {
		return orderRepository.findById(id);

	}

	public List<OrderEntity> getOrdersByCustomerPages(int num, int size, int id){
		Sort sortable = Sort.by("timebought").descending();
		Pageable pageable = PageRequest.of(num, size, sortable);
		return orderRepository.findByCustomersId(pageable, id);
	}
	public List<OrderEntity> getOrderPage(int num, int size){
		Sort sortable = Sort.by("timebought").descending().and(Sort.by("status"));
		Pageable pageable = PageRequest.of(num, size, sortable);
		return orderRepository.findAll(pageable).stream().collect(Collectors.toList());
	}
	
	@Transactional
	public OrderEntity createOrder(OrderDTO orderDTO) {
		OrderEntity orderEntity = new OrderEntity(orderDTO);
		orderEntity.setTimebought(LocalDateTime.now());
		OrderEntity orderSaved = orderRepository.save(orderEntity);
		for (OrderDetailDTO detailDTO : orderDTO.getOrderDetails()) {
			OrderDetailEntity detail = new OrderDetailEntity(detailDTO);

			OrderDetailsKey id = new OrderDetailsKey(orderSaved.getId(),detailDTO.getProductId() );
			detail.setId(id);
			boolean result = orderDetailService.createDetail(detail);
			if (!result) throw new ObjectPropertiesIllegalException("Failed in create detail order");

		}
		return orderRepository.getById(orderSaved.getId());
	}

	@Transactional
	public boolean deleteOrder(int id) {
		OrderEntity orderEntity = getOrders(id).get();
		if (!orderEntity.isStatus()) { // False = Not delivery yet
			for (OrderDetailEntity detail : orderDetailService.getDetailOrderByOrderId(id)) {
				productService.updateProductQuantity(detail.getProduct().getId(), detail.getAmmount());
			}
		}
		orderRepository.delete(orderEntity);
		return true;
	}

	public boolean updateOrder(OrderDTO orderDTO) {
//		try {
		/*
		 * * Sử dụng cách này vì khi thay đổi Order thì số lượng product bán ra sẽ thay
		 * đổi.
		 * 
		 */
		int orderId = orderDTO.getId();
		OrderEntity orderCheck = getOrders(orderId).get(); // Nếu không có Order sẽ gây ra lỗi NoSuchElementException
														   // sẽ được catch ở Controller
//		orderDetailService.getDetailOrder(orderId).forEach(deOr -> {
//			
//		});
		for (OrderDetailEntity detail : orderDetailService.getDetailOrderByOrderId(orderId)) {
			boolean result = orderDetailService.deleteDetail(detail);
			if (!result) return false;
		}
		for (OrderDetailDTO detailDTO : orderDTO.getOrderDetails()) {
			OrderDetailEntity detail = new OrderDetailEntity(detailDTO);
			//OrderDetailsKey id = new OrderDetailsKey(orderDTO.getId(), detail.getProduct().getId());
			//detail.setId(id);
			orderDetailService.createDetail(detail);
		}
		orderRepository.save(new OrderEntity(orderDTO));
		return true;
//		for (OrderDetailEntity detail : orderDetailService.getDetailOrder(orderDTO.getId())) {
//			OrderDetailDTO checkDetail = orderDTO.getOrderDetails().stream()
//					.filter(o -> o.getProduct().getId().equals(detail.getProduct().getId())).findFirst().orElse(null);
//			if (checkDetail == null) {
//				OrderDetailEntity detailEntity = new OrderDetailEntity(orderDTO);
//				OrderDetailsKey id = new OrderDetailsKey(orderEntity.getId(), detailEntity.getProduct().getId());
//				detail.setId(id);
//				orderDetailService.createOrder(detailEntity);
//			}
//			else {
//			int quantityChange = detail.getAmmount() - checkDetail.getAmmount();
//			productService.updateProductQuantity(detail.getProduct().getId(), detail.getAmmount());
//			}
//		}

//			boolean checkProduct = (orderDTO.getProducts().getId().equals(orderEntity.getProducts().getId()));
//			if (!checkProduct) {
//				// Increase quantity of old Product
//				productService.updateProductQuantity(orderEntity.getProducts().getId(), orderEntity.getQuantity());
//				// Decrease quantity of new Product
//				productService.updateProductQuantity(orderDTO.getProducts().getId(), orderDTO.getQuantity() * (-1));
//			} else if (quantityChange != 0 && checkProduct) {
//				productService.updateProductQuantity(orderEntity.getProducts().getId(), quantityChange);
//			}
//			orderRepository.save(new OrderEntity(orderDTO));
//
//		} catch (HttpMessageNotReadableException | JsonParseException | NullPointerException ex) {
//			throw new JsonGetDataException(ex.getMessage());
//		} 
	}

	public List<OrderEntity> findOrderByCustomer(int num, int size, String email) {
		// PersonEntity person = personService.getPerson(email);
		Sort sortable = Sort.by("timebought").descending();
		Pageable pageable = PageRequest.of(num, size, sortable);
		return orderRepository.findByCustomersEmail(pageable, email);
	}

//	public OrderEntity findOrderByProducts(String id) {
//		// return orderRepository.findByProductsId(id);
//		return new OrderEntity();
//	}
}
