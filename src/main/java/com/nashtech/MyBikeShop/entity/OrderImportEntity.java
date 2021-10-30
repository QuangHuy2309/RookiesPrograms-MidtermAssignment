package com.nashtech.MyBikeShop.entity;

import java.time.LocalDateTime;
import java.util.Objects;
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
import com.nashtech.MyBikeShop.DTO.OrderImportDTO;

@Entity
@Table(name = "orderimport")
public class OrderImportEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "timeimport")
	private LocalDateTime timeimport;

//	@Column(name = "totalcost")
//	private Double totalCost;

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

	public OrderImportEntity(int id, boolean status, PersonEntity employee,
			Set<OrderImportDetailEntity> orderImportDetails) {
		super();
		this.id = id;
		this.status = status;
		this.employee = employee;
		this.orderImportDetails = orderImportDetails;
	}

	public OrderImportEntity(int id, LocalDateTime timeimport, boolean status, PersonEntity employee,
			Set<OrderImportDetailEntity> orderDetails) {
		super();
		this.id = id;
		this.timeimport = timeimport;
//		this.totalCost = totalCost;
		this.status = status;
		this.employee = employee;
		this.orderImportDetails = orderDetails;
	}

	public OrderImportEntity(OrderImportDTO order) {
		super();
		this.id = order.getId();
//		this.totalCost = order.getTotalCost();
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

//	public Double getTotalCost() {
//		return totalCost;
//	}
//
//	public void setTotalCost(Double totalCost) {
//		if (totalCost < 0) {
//			throw new IllegalArgumentException("Total must not below zero");
//		}
//		this.totalCost = totalCost;
//	}

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

	@Override
	public int hashCode() {
		return Objects.hash(employee, id, orderImportDetails, status, timeimport);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderImportEntity other = (OrderImportEntity) obj;
		return Objects.equals(employee, other.employee) && id == other.id
				&& Objects.equals(orderImportDetails, other.orderImportDetails) && status == other.status
				&& Objects.equals(timeimport, other.timeimport);
	}

	@Override
	public String toString() {
		return "OrderImportEntity [id=" + id + ", timeimport=" + timeimport + ", status=" + status + ", employee="
				+ employee;
	}

}
