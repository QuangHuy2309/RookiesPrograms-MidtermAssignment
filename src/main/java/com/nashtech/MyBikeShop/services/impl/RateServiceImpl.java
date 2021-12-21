package com.nashtech.MyBikeShop.services.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.OptionalDouble;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.nashtech.MyBikeShop.DTO.RateDTO;
import com.nashtech.MyBikeShop.entity.PersonEntity;
import com.nashtech.MyBikeShop.entity.ProductEntity;
import com.nashtech.MyBikeShop.entity.RateEntity;
import com.nashtech.MyBikeShop.exception.ObjectAlreadyExistException;
import com.nashtech.MyBikeShop.exception.ObjectNotFoundException;
import com.nashtech.MyBikeShop.repository.PersonRepository;
import com.nashtech.MyBikeShop.repository.ProductRepository;
import com.nashtech.MyBikeShop.repository.RateRepository;
import com.nashtech.MyBikeShop.services.OrderService;
import com.nashtech.MyBikeShop.services.RateService;

@Service
public class RateServiceImpl implements RateService {
	@Autowired
	RateRepository rateRepo;

	@Autowired
	ProductRepository prodRepo;

	@Autowired
	PersonRepository personRepo;

	@Autowired
	OrderService orderService;

	private static final Logger logger = Logger.getLogger(RateServiceImpl.class);

	public List<RateEntity> getRateByProduct(String id) {
		return rateRepo.findByProductIdAndCustomerStatusNot(id, false);
	}

	public boolean checkExist(String prodId, int customerId) {
		return (!rateRepo.existsByProductIdAndCustomerId(prodId, customerId)) && (rateRepo.checkUserOrdered(prodId, customerId) > 0)
				&& (orderService.checkOrderedByProductAndCustomerId(prodId, customerId));
	}

	public RateEntity createRate(RateDTO rateDTO) {
		boolean checkExist = rateRepo.existsByProductIdAndCustomerId(rateDTO.getProductId(), rateDTO.getCustomerId());
		if (checkExist) {
			logger.error("Account id " + rateDTO.getCustomerId() + " review product with id " + rateDTO.getProductId()
					+ " failed: This account had reviewed before");
			throw new ObjectAlreadyExistException("Exist a review with this customer on this product");

		} else {
			ProductEntity prod;
			PersonEntity person;
			try {
				prod = prodRepo.findByIdIgnoreCase(rateDTO.getProductId()).get();
			} catch (NoSuchElementException ex) {
				logger.error("Account id " + rateDTO.getCustomerId() + " delete rate with ID " + rateDTO.getId()
						+ "failed: Not found Rate with product ID: " + rateDTO.getProductId());
				throw new ObjectNotFoundException(
						"Not found Rate with product ID: " + rateDTO.getProductId());
			}
			try {
				person = personRepo.findById(rateDTO.getCustomerId()).get();
			} catch (NoSuchElementException ex) {
				logger.error("Account id " + rateDTO.getCustomerId() + " delete rate with ID " + rateDTO.getId()
						+ "failed: Not found Rate with CustomerId: " + String.valueOf(rateDTO.getCustomerId()));
				throw new ObjectNotFoundException(
						"Not found Rate with CustomerId: " + String.valueOf(rateDTO.getCustomerId()));
			}
			try {
				RateEntity rate = new RateEntity(rateDTO);
				rate.setProduct(prod);
				rate.setCustomer(person);
				rate.setDateReview(java.sql.Date.valueOf(LocalDate.now()));
				logger.info("Account id " + person.getId() + " create review success");
				return rateRepo.save(rate);

			} catch (NoSuchElementException ex) {
				logger.error("Not found person with id: " + rateDTO.getCustomerId());
				throw new ObjectNotFoundException("Not found person with id: " + rateDTO.getCustomerId());
			}
		}
	}

	public List<RateEntity> getRateProductPage(String id, int pageNum, int size) {
		Sort sortable = Sort.by("dateReview").descending();
		Pageable pageable = PageRequest.of(pageNum, size, sortable);
		return rateRepo.findByProductIdAndCustomerStatusNot(pageable, id, false);
	}

	public boolean deleteRate(int id, String userId) {
		RateEntity rate;
		ProductEntity prod;
		PersonEntity person;
		try {
			rate = rateRepo.findById(id).get();
		} catch (NoSuchElementException ex) {
			logger.error("Account id " + userId + " delete rate with ID " + id
					+ "failed: Not found Rate with ID: " + String.valueOf(id));
			throw new ObjectNotFoundException(
					"Not found Rate with ID: " + String.valueOf(id));
		}
		try {
			prod = prodRepo.findByIdIgnoreCase(rate.getProduct().getId()).get();
		} catch (NoSuchElementException ex) {
			logger.error("Account id " + userId + " delete rate with ID " + id
					+ "failed: Not found Rate with product ID: " + rate.getProduct().getId());
			throw new ObjectNotFoundException(
					"Not found Rate with product ID: " + rate.getProduct().getId());
		}
		try {
			person = personRepo.findById(rate.getCustomer().getId()).get();
		} catch (NoSuchElementException ex) {
			logger.error("Account id " + userId + " delete rate with ID " + id
					+ "failed: Not found Rate with CustomerId: " + String.valueOf(rate.getCustomer().getId()));
			throw new ObjectNotFoundException(
					"Not found Rate with CustomerId: " + String.valueOf(rate.getCustomer().getId()));
		}
			prod.getReviews().remove(rate);
			person.getReviews().remove(rate);
			prodRepo.save(prod);
			personRepo.save(person);
			rateRepo.delete(rate);
			logger.info("Account id " + userId + " delete rate with ID " + id + " success");
			return true;
		
	}

	public boolean updateRate(RateDTO rateDTO) {
		boolean check = rateRepo.existsById(rateDTO.getId());
		if (check) {
			RateEntity rate = new RateEntity(rateDTO);
			rate.setId(rateDTO.getId());
			rate.setDateReview(java.sql.Date.valueOf(LocalDate.now()));
			rateRepo.save(rate);
			return true;
		}
		return false;
	}

	public int getNumRate(String id) {
		return rateRepo.countByProductIdAndCustomerStatusNot(id, false);
	}

	public double getAverageRateNumByProduct(String id) {
		List<RateEntity> list = rateRepo.findByProductIdAndCustomerStatusNot(id, false);
		OptionalDouble avg = list.stream().map(rate -> rate.getRateNum()).mapToDouble(a -> a).average();
		return avg.isPresent() ? avg.getAsDouble() : 0.0;
	}
}
