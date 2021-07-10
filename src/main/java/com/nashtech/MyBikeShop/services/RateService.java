package com.nashtech.MyBikeShop.services;

import java.util.List;
import java.util.Set;

import com.nashtech.MyBikeShop.DTO.RateDTO;
import com.nashtech.MyBikeShop.entity.RateEntity;
import com.nashtech.MyBikeShop.entity.RateEntity.RateKey;

public interface RateService {
	public List<RateEntity> getRateByProduct(String id);
	public RateEntity createRate(RateDTO rate);
	public boolean deleteRate(RateKey id);
	public boolean updateRate(RateDTO rate);
}
