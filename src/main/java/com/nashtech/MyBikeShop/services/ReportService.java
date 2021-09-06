package com.nashtech.MyBikeShop.services;

import java.util.List;

public interface ReportService {
	public List<Float> profitByYear(int year);
	
	public List<Float> purchaseCostByYear(int year);
}
