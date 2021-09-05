package com.nashtech.MyBikeShop.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nashtech.MyBikeShop.DTO.OrderImportDTO;
import com.nashtech.MyBikeShop.DTO.OrderImportDetailDTO;
import com.nashtech.MyBikeShop.entity.OrderImportDetailEntity;
import com.nashtech.MyBikeShop.entity.OrderImportEntity;
import com.nashtech.MyBikeShop.entity.PersonEntity;
import com.nashtech.MyBikeShop.entity.ProductEntity;
import com.nashtech.MyBikeShop.repository.OrderImportRepository;
import com.nashtech.MyBikeShop.services.OrderImportDetailService;
import com.nashtech.MyBikeShop.services.OrderImportService;
import com.nashtech.MyBikeShop.services.PersonService;
import com.nashtech.MyBikeShop.services.ProductService;

@Service
public class OrderImportServiceImpl implements OrderImportService {
	@Autowired
	OrderImportRepository orderImportRepo;

	@Autowired
	ModelMapper mapper;

	@Autowired
	ProductService productService;

	@Autowired
	PersonService personService;

	@Autowired
	OrderImportDetailService orderImportDetailService;

	@Override
	@Transactional
	public OrderImportEntity createOrderImport(OrderImportEntity orderImport) {
		if (orderImport.isStatus()) {
			changeProductQuantityByDetailList(orderImport.getOrderImportDetails(), true);
		}
		orderImport.setTimeimport(LocalDateTime.now());
		return orderImportRepo.save(orderImport);
	}

	@Override
	public OrderImportEntity convertToEntity(OrderImportDTO orderImportDto) {
		OrderImportEntity orderImport = mapper.map(orderImportDto, OrderImportEntity.class);
		PersonEntity employee = personService.getPerson(orderImportDto.getEmployeeEmail());
		orderImport.setEmployee(employee);
		return orderImport;
	}

	@Override
	public OrderImportDTO convertToDto(OrderImportEntity orderImport) {
		OrderImportDTO importDto = mapper.map(orderImport, OrderImportDTO.class);
		importDto.setEmployeeEmail(orderImport.getEmployee().getEmail());
		importDto.setEmployeeFullName(orderImport.getEmployee().getFullname());
		Set<OrderImportDetailEntity> orderImportDetails = orderImport.getOrderImportDetails();
		Set<OrderImportDetailDTO> orderImportDetailsDto = orderImportDetails.stream()
				.map(orderImportDetailService::convertToDto).collect(Collectors.toSet());
		importDto.setOrderImportDetails(orderImportDetailsDto);
		return importDto;
	}

	@Override
	public List<OrderImportEntity> getOrderImportPage(int num, int size) {
		Sort sortable = Sort.by("timeimport").descending();
		Pageable pageable = PageRequest.of(num, size, sortable);
		return orderImportRepo.findAll(pageable).stream().collect(Collectors.toList());
	}

	@Override
	public OrderImportEntity findOrderImportById(int importId) {
		return orderImportRepo.findById(importId).orElse(null);
	}

	private void changeProductQuantityByDetailList(Set<OrderImportDetailEntity> importDetailList, boolean isAdd) {
		for (OrderImportDetailEntity importDetail : importDetailList) {
			ProductEntity product = productService.getProduct(importDetail.getProduct().getId()).orElse(null);
			int productNewQuantity = product.getQuantity();
			if (isAdd) {
				productNewQuantity += importDetail.getAmmount();
			} else {
				productNewQuantity -= importDetail.getAmmount();
			}
			product.setQuantity(productNewQuantity);

			productService.updateProductWithoutCheckAnything(product);
		}
	}

	@Override
	@Transactional
	public OrderImportEntity updateOrderImport(OrderImportDTO orderImportDto, int orderImportId) {
		OrderImportEntity orderImport = findOrderImportById(orderImportId);
		
		if(!orderImport.isStatus()&&orderImportDto.isStatus()) {
			changeProductQuantityByDetailList(orderImport.getOrderImportDetails(), true);
		}
		
		orderImport.setStatus(true);
		
		return orderImportRepo.save(orderImport);
	}

}
