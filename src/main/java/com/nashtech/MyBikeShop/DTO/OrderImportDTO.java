package com.nashtech.MyBikeShop.DTO;

import java.time.LocalDateTime;
import java.util.Set;

public class OrderImportDTO {
	private int id;
	private LocalDateTime timeimport ;
	private Double totalCost;
	private boolean status;
	private String employeeEmail;
	private String employeeFullName;
	Set<OrderImportDetailDTO> orderImportDetails;
	
	public OrderImportDTO() {
	}
	
	public OrderImportDTO(int id, LocalDateTime timeimport,Double totalCost, boolean status, String employeeEmail,
			String employeeFullName, Set<OrderImportDetailDTO> orderImportDetails) {
		super();
		this.id = id;
		this.timeimport = timeimport;
		this.status = status;
		this.employeeEmail = employeeEmail;
		this.employeeFullName = employeeFullName;
		this.orderImportDetails = orderImportDetails;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public LocalDateTime getTimeimport() {
		return timeimport;
	}
	public void setTimeimport(LocalDateTime timeimport) {
		this.timeimport = timeimport;
	}
	public Double getTotalCost() {
		return totalCost;
	}
	public void setTotalCost(Double totalCost) {
		this.totalCost = totalCost;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getEmployeeEmail() {
		return employeeEmail;
	}
	public void setEmployeeEmail(String employeeEmail) {
		this.employeeEmail = employeeEmail;
	}
	public String getEmployeeFullName() {
		return employeeFullName;
	}
	public void setEmployeeFullName(String employeeFullName) {
		this.employeeFullName = employeeFullName;
	}
	public Set<OrderImportDetailDTO> getOrderImportDetails() {
		return orderImportDetails;
	}
	public void setOrderImportDetails(Set<OrderImportDetailDTO> orderImportDetails) {
		this.orderImportDetails = orderImportDetails;
	}
	
	
}
