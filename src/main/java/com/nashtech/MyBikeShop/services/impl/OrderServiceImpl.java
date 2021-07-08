package com.nashtech.MyBikeShop.services.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParseException;
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

	public OrderEntity getOrders(int id) {
		return orderRepository.findById(id)
				.orElseThrow(() -> new ObjectNotFoundException("Could not find order with Id: " + id));

	}

	@Transactional
	public String createOrder(OrderDTO orderDTO) {
		OrderEntity orderEntity = new OrderEntity(orderDTO);
		orderEntity.setTimebought(LocalDateTime.now());
		OrderEntity orderEntitySaved = orderRepository.save(orderEntity);
		for (OrderDetailDTO detailDTO : orderDTO.getOrderDetails()) {
			OrderDetailEntity detail = new OrderDetailEntity(detailDTO);
			OrderDetailsKey id = new OrderDetailsKey(orderEntitySaved.getId(), detail.getProduct().getId());
			detail.setId(id);
			orderDetailService.createDetail(detail);

		}
		return "Success";
	}

	@Transactional
	public String deleteOrder(int id) {
		try {
			OrderEntity orderEntity = getOrders(id);
			if (!orderEntity.isStatus()) { // False = Not delivery yet
				for (OrderDetailEntity detail : orderDetailService.getDetailOrder(id)) {
					productService.updateProductQuantity(detail.getProduct().getId(), detail.getAmmount());
				}
			}
			orderRepository.delete(orderEntity);
			return "Success!";
		} catch (MethodArgumentTypeMismatchException | NumberFormatException ex) {
			throw new JsonGetDataException(ex.getMessage());
		}
	}

	public String updateOrder(OrderDTO orderDTO) {
//		try {
		/*
		 * * Sử dụng cách này vì khi thay đổi Order thì số lượng product bán ra sẽ thay đổi. 
		 */
		orderDetailService.getDetailOrder(orderDTO.getId()).forEach(deOr -> orderDetailService.deleteDetail(deOr)); 
		for (OrderDetailDTO detailDTO : orderDTO.getOrderDetails()) {
			OrderDetailEntity detail = new OrderDetailEntity(detailDTO);
			OrderDetailsKey id = new OrderDetailsKey(orderDTO.getId(), detail.getProduct().getId());
			detail.setId(id);
			orderDetailService.createDetail(detail);
		}
		orderRepository.save(new OrderEntity(orderDTO));
		return "Success";
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

	public List<OrderEntity> findOrderByCustomer(String email) {
		// PersonEntity person = personService.getPerson(email);
		return orderRepository.findByCustomersEmail(email);
	}

	public OrderEntity findOrderByProducts(String id) {
		// return orderRepository.findByProductsId(id);
		return new OrderEntity();
	}
}
