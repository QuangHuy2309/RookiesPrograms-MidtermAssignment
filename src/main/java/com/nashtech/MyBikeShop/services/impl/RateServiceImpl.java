package com.nashtech.MyBikeShop.services.impl;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.nashtech.MyBikeShop.DTO.RateDTO;
import com.nashtech.MyBikeShop.entity.RateEntity;
import com.nashtech.MyBikeShop.entity.RateEntity.RateKey;
import com.nashtech.MyBikeShop.exception.ObjectNotFoundException;
import com.nashtech.MyBikeShop.repository.RateRepository;
import com.nashtech.MyBikeShop.services.RateService;

@Service
public class RateServiceImpl implements RateService {
	@Autowired
	RateRepository rateRepo;

	public List<RateEntity> getRateByProduct(String id) {
		return rateRepo.findByIdProductId(id);
	}

	public RateEntity createRate(RateDTO rateDTO) {
		RateEntity rate = new RateEntity(rateDTO);
		rate.setDateReview(java.sql.Date.valueOf(LocalDate.now()));
		return rateRepo.save(rate);
	}

	public List<RateEntity> getRateProductPage(String id, int pageNum, int size) {
		Sort sortable = Sort.by("dateReview").descending();
		Pageable pageable = PageRequest.of(pageNum, size, sortable);
		return rateRepo.findByIdProductId(pageable, id);
	}

	public boolean deleteRate(RateKey id) {
		try {
			rateRepo.deleteById(id);
			return true;
		} catch (EmptyResultDataAccessException ex) {
			throw new ObjectNotFoundException(
					"Not found Rate with CustomerId: " + id.getCustomerId() + " - ProductId: " + id.getProductId());
		}
	}

	public boolean updateRate(RateDTO rateDTO) {
		try {
			RateEntity rateCheck = rateRepo.findById(new RateKey(rateDTO.getCustomerId(), rateDTO.getProductId()))
					.get();
		} catch (NoSuchElementException ex) {
			throw new ObjectNotFoundException("Not found Rate with CustomerId: " + rateDTO.getCustomerId()
					+ " - ProductId: " + rateDTO.getProductId());
		}
		RateEntity rate = new RateEntity(rateDTO);
		rate.setDateReview(java.sql.Date.valueOf(LocalDate.now()));
		rateRepo.save(rate);
		return true;
	}

	public int getNumRate(String id) {
		return rateRepo.countByIdProductId(id);
	}
}
