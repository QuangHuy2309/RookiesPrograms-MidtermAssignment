package com.nashtech.MyBikeShop.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nashtech.MyBikeShop.DTO.OrderImportDetailDTO;
import com.nashtech.MyBikeShop.entity.OrderImportDetailEntity;
import com.nashtech.MyBikeShop.entity.OrderImportDetailEntity.OrderImportDetailsKey;
import com.nashtech.MyBikeShop.entity.OrderImportEntity;
import com.nashtech.MyBikeShop.entity.ProductEntity;
import com.nashtech.MyBikeShop.services.OrderImportDetailService;
import com.nashtech.MyBikeShop.services.OrderImportService;
import com.nashtech.MyBikeShop.services.PersonService;
import com.nashtech.MyBikeShop.services.ProductService;

@Service
public class OrderImportDetailServiceImpl implements OrderImportDetailService {
	@Autowired
	ModelMapper mapper;
	
	@Autowired
	ProductService productService;
	
	@Autowired
	PersonService personService;
	
	@Autowired
	OrderImportService importService;

	@Override
	public OrderImportDetailEntity convertToEntity(OrderImportDetailDTO orderImportDetailDto) {
		OrderImportDetailEntity importDetail = mapper.map(orderImportDetailDto, OrderImportDetailEntity.class);
		OrderImportDetailsKey importKey = new OrderImportDetailsKey(orderImportDetailDto.getOrderImportId(),
				orderImportDetailDto.getProductId());
		importDetail.setId(importKey);
		ProductEntity product = productService.getProduct(orderImportDetailDto.getProductId()).orElse(null);
		OrderImportEntity orderImport = importService.findOrderImportById(orderImportDetailDto.getOrderImportId());
		importDetail.setProduct(product);
		importDetail.setOrder(orderImport);
		importDetail.setAmmount(orderImportDetailDto.getAmount());
		importDetail.setPrice(orderImportDetailDto.getUnitprice());
		return importDetail;
	}

	@Override
	public OrderImportDetailDTO convertToDto(OrderImportDetailEntity orderImportDetail) {
		OrderImportDetailDTO importDetailDto = mapper.map(orderImportDetail, OrderImportDetailDTO.class);
		importDetailDto.setOrderImportId(orderImportDetail.getId().getOrderId());
		importDetailDto.setProductId(orderImportDetail.getId().getProductId());
		importDetailDto.setAmount(orderImportDetail.getAmmount());
		importDetailDto.setUnitprice(orderImportDetail.getPrice());
		return importDetailDto;
	}

}
