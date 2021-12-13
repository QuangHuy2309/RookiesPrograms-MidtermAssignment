package com.nashtech.MyBikeShop.services.impl;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.OptionalDouble;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.nashtech.MyBikeShop.DTO.RateDTO;
import com.nashtech.MyBikeShop.controller.CategoriesController;
import com.nashtech.MyBikeShop.entity.PersonEntity;
import com.nashtech.MyBikeShop.entity.ProductEntity;
import com.nashtech.MyBikeShop.entity.RateEntity;
import com.nashtech.MyBikeShop.entity.RateEntity.RateKey;
import com.nashtech.MyBikeShop.exception.ObjectAlreadyExistException;
import com.nashtech.MyBikeShop.exception.ObjectNotFoundException;
import com.nashtech.MyBikeShop.repository.OrderRepository;
import com.nashtech.MyBikeShop.repository.PersonRepository;
import com.nashtech.MyBikeShop.repository.ProductRepository;
import com.nashtech.MyBikeShop.repository.RateRepository;
import com.nashtech.MyBikeShop.services.OrderService;
import com.nashtech.MyBikeShop.services.PersonService;
import com.nashtech.MyBikeShop.services.ProductService;
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
		return rateRepo.findByIdProductIdAndCustomerStatusNot(id, false);
	}

	public boolean checkExist(RateKey id) {
		return (!rateRepo.existsById(id)) && (rateRepo.checkUserOrdered(id.getProductId(), id.getCustomerId()) > 0)
				&& (orderService.checkOrderedByProductAndCustomerId(id.getProductId(), id.getCustomerId()));
	}

	public RateEntity createRate(RateDTO rateDTO) {
		boolean checkExist = rateRepo.existsById(new RateKey(rateDTO.getCustomerId(), rateDTO.getProductId()));
		if (checkExist) {
			logger.error("Account id " + rateDTO.getCustomerId() + " review product with id " + rateDTO.getProductId()
					+ " failed: This account had reviewed before");
			throw new ObjectAlreadyExistException("Exist a review with this customer on this product");

		} else {
			try {
				PersonEntity person = personRepo.findById(rateDTO.getCustomerId()).get();

				RateEntity rate = new RateEntity(rateDTO);
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
		return rateRepo.findByIdProductIdAndCustomerStatusNot(pageable, id, false);
	}

	public boolean deleteRate(RateKey id, String userId) {
		try {
			ProductEntity prod = prodRepo.findByIdIgnoreCase(id.getProductId()).get();
			PersonEntity person = personRepo.findById(id.getCustomerId()).get();
			RateEntity rate = rateRepo.findById(id).get();
			prod.getReviews().remove(rate);
			person.getReviews().remove(rate);
			prodRepo.save(prod);
			personRepo.save(person);
			rateRepo.delete(rate);
			logger.info("Account id " + userId + " delete rate with ID " + id.toString() + " success");
			return true;
		} catch (EmptyResultDataAccessException | NoSuchElementException ex) {
			logger.error("Account id " + userId + " delete rate with ID " + id.toString()
					+ "failed: Not found Rate with CustomerId: " + id.getCustomerId() + " - ProductId: "
					+ id.getProductId());
			throw new ObjectNotFoundException(
					"Not found Rate with CustomerId: " + id.getCustomerId() + " - ProductId: " + id.getProductId());
		}
	}

	public boolean updateRate(RateDTO rateDTO) {
		boolean check = rateRepo.existsById(new RateKey(rateDTO.getCustomerId(), rateDTO.getProductId()));
		if (check) {
			RateEntity rate = new RateEntity(rateDTO);
			rate.setDateReview(java.sql.Date.valueOf(LocalDate.now()));
			rateRepo.save(rate);
			return true;
		}
		return false;
	}

	public int getNumRate(String id) {
		return rateRepo.countByIdProductIdAndCustomerStatusNot(id, false);
	}

	public double getAverageRateNumByProduct(String id) {
		List<RateEntity> list = rateRepo.findByIdProductIdAndCustomerStatusNot(id, false);
		OptionalDouble avg = list.stream().map(rate -> rate.getRateNum()).mapToDouble(a -> a).average();
		return avg.isPresent() ? avg.getAsDouble() : 0.0;
	}
}
