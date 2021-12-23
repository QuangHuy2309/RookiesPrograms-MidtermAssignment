package com.nashtech.MyBikeShop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nashtech.MyBikeShop.DTO.ReportProdProcess;
import com.nashtech.MyBikeShop.DTO.ReportTopProduct;
import com.nashtech.MyBikeShop.services.OrderImportService;
import com.nashtech.MyBikeShop.services.OrderService;
import com.nashtech.MyBikeShop.services.ReportService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class ReportController {
	@Autowired
	ReportService reportService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	OrderImportService importService;
	
	@GetMapping("/report/profit/{year}")
	@PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
	public List<Float> profitByYear(@PathVariable(name = "year") int year){
		return reportService.profitByYear(year);
	}
	//Revenue By Month
	@GetMapping("/report/profitByMonth")
	@PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
	public List<Float> profitByYear(@RequestParam(name = "fromMonth") String fromMonth,
			@RequestParam(name = "toMonth") String toMonth){
		return reportService.profitMonth(fromMonth,toMonth);
	}
	
	
	// Profit by Month
	@GetMapping("/report/profit")
	@PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
	public double profitByMonthAndYear(@RequestParam(name = "month") int month,
			@RequestParam(name = "year") int year){
		return reportService.profitAvgMonth(month,year);
	}
	
	@GetMapping("/report/purchasecost/{year}")
	@PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
	public List<Float> purchaseCostByYear(@PathVariable(name = "year") int year){
		return reportService.purchaseCostByYear(year);
	}
	
	@GetMapping("/report/topProduct")
	@PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
	public List<ReportTopProduct> topProdByMonth(@RequestParam(name = "fromMonth") String fromMonth,
			@RequestParam(name = "toMonth") String toMonth){
		if (fromMonth.compareTo(toMonth) == 1) {
			String temp = fromMonth;
			fromMonth = toMonth;
			toMonth = temp;
		}
		return reportService.topProductByMonth(fromMonth,toMonth);
	}
	
	@GetMapping("/report/productProccess/{cateId}")
	@PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
	public List<ReportProdProcess> productProcess(@PathVariable(name = "cateId") int cateId){
		return reportService.productProccess(cateId);
	}
}
