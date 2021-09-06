package com.nashtech.MyBikeShop.services;

import com.nashtech.MyBikeShop.DTO.OrderImportDetailDTO;
import com.nashtech.MyBikeShop.entity.OrderImportDetailEntity;

public interface OrderImportDetailService {
	
	public OrderImportDetailEntity convertToEntity (OrderImportDetailDTO orderImportDetailDto);
	
	public OrderImportDetailDTO convertToDto (OrderImportDetailEntity orderImportDetail);
}
