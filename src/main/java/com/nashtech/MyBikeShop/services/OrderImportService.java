package com.nashtech.MyBikeShop.services;

import java.util.List;

import com.nashtech.MyBikeShop.DTO.OrderImportDTO;
import com.nashtech.MyBikeShop.entity.OrderImportEntity;

public interface OrderImportService {
	public OrderImportEntity createOrderImport (OrderImportEntity orderImport);
	
	public OrderImportEntity convertToEntity (OrderImportDTO orderImportDto);
	
	public OrderImportDTO convertToDto (OrderImportEntity orderImport);
	
	public List<OrderImportEntity> getOrderImportPage(int num, int size);
	
	public OrderImportEntity findOrderImportById (int importId);
	
	public OrderImportEntity updateOrderImport(OrderImportDTO orderImportDto, int orderImportId);
	
	public boolean deleteOrderImport (int orderImportId);
	
	public float purchaseCostByMonth (int month, int year);
	
}
