package com.nashtech.MyBikeShop.services;

import java.util.List;

import com.nashtech.MyBikeShop.DTO.RateDTO;
import com.nashtech.MyBikeShop.entity.RateEntity;

public interface RateService {
	public List<RateEntity> getRateByProduct(String id);

	public List<RateEntity> getRateProductPage(String id, int pageNum, int size);

	public RateEntity createRate(RateDTO rate);

	public boolean deleteRate(int id, String email);

	public boolean updateRate(RateDTO rate);

	public int getNumRate(String id);

	public boolean checkExist(String prodId, int customerId);

	public double getAverageRateNumByProduct(String id);
}
