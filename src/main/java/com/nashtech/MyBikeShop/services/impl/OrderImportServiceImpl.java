package com.nashtech.MyBikeShop.services.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.nashtech.MyBikeShop.DTO.OrderImportDTO;
import com.nashtech.MyBikeShop.DTO.OrderImportDetailDTO;
import com.nashtech.MyBikeShop.controller.CategoriesController;
import com.nashtech.MyBikeShop.entity.OrderImportDetailEntity;
import com.nashtech.MyBikeShop.entity.OrderImportDetailEntity.OrderImportDetailsKey;
import com.nashtech.MyBikeShop.entity.OrderImportEntity;
import com.nashtech.MyBikeShop.entity.PersonEntity;
import com.nashtech.MyBikeShop.entity.ProductEntity;
import com.nashtech.MyBikeShop.exception.ObjectNotFoundException;
import com.nashtech.MyBikeShop.exception.ObjectPropertiesIllegalException;
import com.nashtech.MyBikeShop.repository.OrderImportRepository;
import com.nashtech.MyBikeShop.services.OrderImportDetailService;
import com.nashtech.MyBikeShop.services.OrderImportService;
import com.nashtech.MyBikeShop.services.PersonService;
import com.nashtech.MyBikeShop.services.ProductService;

@Service
public class OrderImportServiceImpl implements OrderImportService {
	@Autowired
	OrderImportRepository orderImportRepo;

	@Autowired
	ModelMapper mapper;

	@Autowired
	ProductService productService;

	@Autowired
	PersonService personService;

	@Autowired
	OrderImportDetailService orderImportDetailService;

	public OrderImportServiceImpl() {
		super();
	}

	public long countTotal() {
		return orderImportRepo.countByStatusNot(false);
	}

	public int getLatestId() {
		return orderImportRepo.findFirstByIdOrderByIdDesc();
	}
	
	public int generateNewId() {
		int id = orderImportRepo.findFirstByIdOrderByIdDesc()+1;
		while (orderImportRepo.existsById(id)) id++;
		return id;
	}
	
	@Override
	@Transactional
	public OrderImportEntity createOrderImport(OrderImportEntity orderImport, int userId) {
		if (orderImport.isStatus()) {
			changeProductQuantityByDetailList(orderImport.getOrderImportDetails(), true, userId);
		}
		orderImport.setId(generateNewId());
		orderImport.setTimeimport(LocalDateTime.now());
		return orderImportRepo.save(orderImport);
	}
	
	@Override
	@Transactional
	public OrderImportEntity createOrderFromXLSS(MultipartFile reapExcelDataFile, String email) {
		Set<OrderImportDetailEntity> detailList = new HashSet<OrderImportDetailEntity>();
		XSSFWorkbook workbook;
		try {
			workbook = new XSSFWorkbook(reapExcelDataFile.getInputStream());
		} catch (IOException e) {
			throw new ObjectNotFoundException("File not found");

		}
		XSSFSheet worksheet = workbook.getSheetAt(0);
		PersonEntity personImport = personService.getPerson(email);
		OrderImportEntity orderImport = new OrderImportEntity();
		orderImport.setTimeimport(LocalDateTime.now());
		orderImport.setEmployee(personImport);
		orderImport.setStatus(true);
		OrderImportEntity orderImport_saved = orderImportRepo.save(orderImport);
		for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
			OrderImportDetailEntity tempDetail = new OrderImportDetailEntity();
			XSSFRow row = worksheet.getRow(i);
			OrderImportDetailsKey keyId = new OrderImportDetailsKey(orderImport_saved.getId(),
					row.getCell(0).getStringCellValue());
			Optional<OrderImportDetailEntity> detailCheck = detailList.stream()
					.filter(detail -> detail.getId().equals(keyId)).findAny();
			if (detailCheck.isPresent())
				throw new ObjectPropertiesIllegalException("Error: File wrong format, duplicate product");
			tempDetail.setId(keyId);
			tempDetail.setAmmount((int) row.getCell(1).getNumericCellValue());
			tempDetail.setPrice((float) row.getCell(2).getNumericCellValue());
			ProductEntity product = productService.getProduct(keyId.getProductId()).orElse(null);
			if (product == null) {
				throw new ObjectNotFoundException("Product ID " + keyId.getProductId() + " not found!");
			}
			tempDetail.setProduct(product);
			tempDetail.setOrder(orderImport_saved);
			detailList.add(tempDetail);
		}
		if (detailList.isEmpty()) {
			deleteOrderImport(orderImport_saved.getId());
			throw new ObjectNotFoundException("Error: File empty");
		}
		orderImport_saved.setOrderImportDetails(detailList);
		System.out.println(orderImport_saved.toString());
		return orderImport_saved; // orderImportRepo.save(orderImport_saved);
	}

	@Override
	public OrderImportEntity convertToEntity(OrderImportDTO orderImportDto) {
		OrderImportEntity orderImport = mapper.map(orderImportDto, OrderImportEntity.class);
		PersonEntity employee = personService.getPerson(orderImportDto.getEmployeeEmail());
		orderImport.setEmployee(employee);
		return orderImport;
	}

	@Override
	public OrderImportDTO convertToDto(OrderImportEntity orderImport) {
		OrderImportDTO importDto = mapper.map(orderImport, OrderImportDTO.class);
		importDto.setEmployeeEmail(orderImport.getEmployee().getEmail());
		importDto.setEmployeeFullName(orderImport.getEmployee().getFullname());
		Double totalCost = 0.0;
		for (OrderImportDetailEntity detail : orderImport.getOrderImportDetails()) {
			totalCost += detail.getAmmount() * detail.getPrice();
		}
		importDto.setTotalCost(totalCost);
		Set<OrderImportDetailEntity> orderImportDetails = orderImport.getOrderImportDetails();
		Set<OrderImportDetailDTO> orderImportDetailsDto = orderImportDetails.stream()
				.map(orderImportDetailService::convertToDto).collect(Collectors.toSet());
		importDto.setOrderImportDetails(orderImportDetailsDto);
		return importDto;
	}

	@Override
	public List<OrderImportEntity> getOrderImportPage(int num, int size) {
		Sort sortable = Sort.by("timeimport").descending();
		Pageable pageable = PageRequest.of(num, size, sortable);
		return orderImportRepo.findByStatusNot(pageable, false).stream().collect(Collectors.toList());
	}

	@Override
	public OrderImportEntity findOrderImportById(int importId) {
		return orderImportRepo.findById(importId).orElse(null);
	}

	public List<OrderImportEntity> searchOrderImportByEmployee(String keyword) {
		return orderImportRepo.searchImportByEmployee(keyword.toUpperCase());
	}

	private void changeProductQuantityByDetailList(Set<OrderImportDetailEntity> importDetailList, boolean isAdd, int userId) {
		for (OrderImportDetailEntity importDetail : importDetailList) {
			ProductEntity product = productService.getProduct(importDetail.getId().getProductId()).orElse(null);
			int productNewQuantity = product.getQuantity();
			if (isAdd) {
				productNewQuantity += importDetail.getAmmount();
			} else {
				productNewQuantity -= importDetail.getAmmount();
			}
			PersonEntity employee = personService.getPerson(userId).get();
			product.setEmployeeUpdate(employee);
			product.setQuantity(productNewQuantity);
			product.setUpdateDate(LocalDateTime.now());
			productService.updateProductWithoutCheckAnything(product);
		}
	}

	@Override
	@Transactional
	public OrderImportEntity updateOrderImport(OrderImportDTO orderImportDto, int orderImportId, int userId) {
		OrderImportEntity orderImport = findOrderImportById(orderImportId);

		if (!orderImport.isStatus() && orderImportDto.isStatus()) {
			changeProductQuantityByDetailList(orderImport.getOrderImportDetails(), true, userId);
		}

		orderImport.setStatus(true);

		return orderImportRepo.save(orderImport);
	}

	@Override
	public boolean deleteOrderImport(int orderImportId) {

		return orderImportRepo.findById(orderImportId).map(order -> {
			for (OrderImportDetailEntity detail : order.getOrderImportDetails()) {
				productService.updateProductQuantityToCancel(detail.getId().getProductId(), detail.getAmmount() * (-1));
			}
//			PersonEntity person = personService.getPerson(order.getEmployee().getId()).get();
//			person.getOrdersImport().remove(order);
//			orderImportRepo.delete(order);
			order.setStatus(false);
			orderImportRepo.save(order);
			return true;
		}).orElse(false);
	}

	@Override
	public float purchaseCostByMonth(int month, int year) {
		Float result = orderImportRepo.purchaseCostByMonth(month, year);
		if (result == null)
			result = (float) 0;
		return result;
	}

}
