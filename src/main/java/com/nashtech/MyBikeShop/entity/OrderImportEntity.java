package com.nashtech.MyBikeShop.entity;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.nashtech.MyBikeShop.DTO.OrderDTO;

@Entity
@Table(name = "orderimport")
public class OrderImportEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "timeimport")
	private LocalDateTime timeimport;

	@Column(name = "totalcost")
	private Float totalCost;

	@Column(name = "status")
	private boolean status;

	@ManyToOne
	@JoinColumn(name = "employeeid")
	private PersonEntity employee;

	@OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	Set<OrderImportDetailEntity> orderImportDetails;

	public OrderImportEntity() {
		super();
	}



	public OrderImportEntity(int id, LocalDateTime timeimport, Float totalCost, boolean status, PersonEntity employee,
			Set<OrderImportDetailEntity> orderDetails) {
		super();
		this.id = id;
		this.timeimport = timeimport;
		this.totalCost = totalCost;
		this.status = status;
		this.employee = employee;
		this.orderImportDetails = orderDetails;
	}



	public OrderImportEntity(OrderDTO order) {
		super();
		this.id = order.getId();
		this.totalCost = order.getTotalCost();
		this.status = order.isStatus();
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



	public PersonEntity getEmployee() {
		return employee;
	}



	public void setEmployee(PersonEntity employee) {
		this.employee = employee;
	}



	public float getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(float totalCost) {
		if (totalCost < 0) {
			throw new IllegalArgumentException("Total must not below zero");
		}
		this.totalCost = totalCost;
	}


	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Set<OrderImportDetailEntity> getOrderImportDetails() {
		return orderImportDetails;
	}



	public void setOrderImportDetails(Set<OrderImportDetailEntity> orderImportDetails) {
		this.orderImportDetails = orderImportDetails;
	}

	public void setTotalCost(Float totalCost) {
		if (totalCost < 0) {
			throw new IllegalArgumentException("Total must not below zero");
		}
		this.totalCost = totalCost;
	}


}
