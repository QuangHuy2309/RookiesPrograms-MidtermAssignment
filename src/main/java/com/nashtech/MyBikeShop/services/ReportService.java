package com.nashtech.MyBikeShop.services;

import java.util.List;

import com.nashtech.MyBikeShop.DTO.ReportProdProcess;
import com.nashtech.MyBikeShop.DTO.ReportTopProduct;
import com.nashtech.MyBikeShop.entity.ProductEntity;

public interface ReportService {
	public List<Float> profitByYear(int year);
	
	public List<Float> purchaseCostByYear(int year);
	
	public List<ReportTopProduct> topProductByMonth(String fromMonth, String toMonth);
	
	public List<ReportProdProcess> productProccess(int cateId);
	
	public List<ProductEntity> hotProduct(int size);
}
