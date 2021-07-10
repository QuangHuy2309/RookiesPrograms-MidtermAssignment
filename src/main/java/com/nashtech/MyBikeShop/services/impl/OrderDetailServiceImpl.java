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
import com.nashtech.MyBikeShop.Utils.StringUtils;
import com.nashtech.MyBikeShop.entity.OrderDetailEntity;
import com.nashtech.MyBikeShop.entity.OrderDetailEntity.OrderDetailsKey;
import com.nashtech.MyBikeShop.entity.OrderEntity;
import com.nashtech.MyBikeShop.entity.PersonEntity;
import com.nashtech.MyBikeShop.entity.ProductEntity;
import com.nashtech.MyBikeShop.exception.JsonGetDataException;
import com.nashtech.MyBikeShop.exception.ObjectAlreadyExistException;
import com.nashtech.MyBikeShop.exception.ObjectNotFoundException;
import com.nashtech.MyBikeShop.repository.OrderDetailRepository;
import com.nashtech.MyBikeShop.repository.OrderRepository;
import com.nashtech.MyBikeShop.services.OrderDetailService;
import com.nashtech.MyBikeShop.services.OrderService;
import com.nashtech.MyBikeShop.services.PersonService;
import com.nashtech.MyBikeShop.services.ProductService;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {
	@Autowired
	OrderDetailRepository orderDetailRepo;

	@Autowired
	OrderService orderService;

	@Autowired
	ProductService productService;

//	@Autowired
//	PersonService personService;

	public OrderDetailServiceImpl(OrderDetailRepository orderDetailRepo) {
		this.orderDetailRepo = orderDetailRepo;
	}

	public List<OrderDetailEntity> retrieveOrders() {
		return orderDetailRepo.findAll();

	}

	public Set<OrderDetailEntity> getDetailOrder(int id) {
		return orderDetailRepo.findByIdOrderId(id);
	}

	@Transactional
	public boolean createDetail(OrderDetailEntity order) {
		// Optional<OrderEntity> order = orderRepository.findById(orderDTO.getId());
//		try {
		// OrderDetailEntity orderDetailEntity = new OrderDetailEntity(orderDTO);
		boolean result = productService.updateProductQuantity(order.getId().getProductId(), order.getAmmount() * (-1));
		if (!result) return false;
		orderDetailRepo.save(order);
		return true;
//		} catch (HttpMessageNotReadableException | JsonParseException | NullPointerException ex) {
//			throw new JsonGetDataException(ex.getMessage());
//		}
//		return orderDetailRepo.save(order);
	}

	@Transactional
	public boolean deleteDetail(OrderDetailEntity orderDetailEntity) {
//		try {
			int orderId = orderDetailEntity.getId().getOrderId();
			OrderEntity orderEntity = orderService.getOrders(orderId).get();
//					.orElseThrow(() -> new ObjectNotFoundException("Could not find Order with id: " + orderId));
			if (!orderEntity.isStatus()) { // False = Not delivery yet
				boolean result = productService.updateProductQuantity(orderDetailEntity.getProduct().getId(),
						orderDetailEntity.getAmmount());
				if (!result) return false;

			}
			orderDetailRepo.delete(orderDetailEntity);
			return true;
//		} catch (MethodArgumentTypeMismatchException | NumberFormatException ex) {
//			throw new JsonGetDataException(ex.getMessage());
//		}
	}

//	public void updateOrder(OrderDTO orderDTO) {
//		try {
//			OrderEntity orderEntity = getOrders(orderDTO.getId());
//			int quantityChange = orderEntity.getQuantity() - orderDTO.getQuantity();
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
//	}
//
//	public List<OrderEntity> findOrderByCustomer(String email) {
//		// PersonEntity person = personService.getPerson(email);
//		return orderRepository.findByCustomersEmail(email);
//	}
//
//	public OrderEntity findOrderByProducts(String id) {
//		//return orderRepository.findByProductsId(id);
//		return new OrderEntity();
//	}
}