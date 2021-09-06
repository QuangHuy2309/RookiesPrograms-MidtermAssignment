package com.nashtech.MyBikeShop.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nashtech.MyBikeShop.services.OrderImportService;
import com.nashtech.MyBikeShop.services.OrderService;
import com.nashtech.MyBikeShop.services.ReportService;

@Service
public class ReportServiceImpl implements ReportService{
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	OrderImportService importService;
	
	public List<Float> profitByYear(int year){
		List<Float> profit = new ArrayList<>();
		for (int i=1; i<=12 ; i++) {
			profit.add(orderService.profitByMonth(i, year));
		}
		return profit;
	}
	
	public List<Float> purchaseCostByYear(int year){
		List<Float> profit = new ArrayList<>();
		for (int i=1; i<=12 ; i++) {
			profit.add(importService.purchaseCostByMonth(i, year));
		}
		return profit;
	}
}
