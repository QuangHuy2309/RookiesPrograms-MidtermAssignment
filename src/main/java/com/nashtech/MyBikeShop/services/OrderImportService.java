package com.nashtech.MyBikeShop.services;

import java.util.List;

import com.nashtech.MyBikeShop.DTO.OrderImportDTO;
import org.springframework.web.multipart.MultipartFile;
import com.nashtech.MyBikeShop.entity.OrderImportDetailEntity;
import com.nashtech.MyBikeShop.entity.OrderImportEntity;

public interface OrderImportService {
	public OrderImportEntity createOrderImport (OrderImportEntity orderImport);
	
	public OrderImportEntity convertToEntity (OrderImportDTO orderImportDto);
	
	public OrderImportDTO convertToDto (OrderImportEntity orderImport);
	
	public List<OrderImportEntity> getOrderImportPage(int num, int size);
	
	public List<OrderImportEntity> searchOrderImportByEmployee(String keyword);
	
	public OrderImportEntity findOrderImportById (int importId);
	
	public OrderImportEntity updateOrderImport(OrderImportDTO orderImportDto, int orderImportId);
	
	public boolean deleteOrderImport (int orderImportId);
	
	public float purchaseCostByMonth (int month, int year);
	
	public OrderImportEntity createOrderFromXLSS(MultipartFile reapExcelDataFile, String email);
	
	public long countTotal();
	
	public int getLatestId();
	
	public int generateNewId();
	
}
