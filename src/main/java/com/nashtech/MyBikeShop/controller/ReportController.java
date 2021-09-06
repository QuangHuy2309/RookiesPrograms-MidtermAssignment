package com.nashtech.MyBikeShop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nashtech.MyBikeShop.services.ReportService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class ReportController {
	@Autowired
	ReportService reportService;
	
	@GetMapping("/report/profit/{year}")
	@PreAuthorize("hasRole('ADMIN')")
	public List<Float> profitByYear(@PathVariable(name = "year") int year){
		return reportService.profitByYear(year);
	}
}
