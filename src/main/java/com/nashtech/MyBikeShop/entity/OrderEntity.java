package com.nashtech.MyBikeShop.entity;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.nashtech.MyBikeShop.DTO.OrderDTO;
import com.nashtech.MyBikeShop.DTO.OrderDetailDTO;
import com.sun.istack.NotNull;

@Entity
@Table(name = "orderbill")
public class OrderEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "timebought")
	private LocalDateTime timebought;

	@Column(name = "totalcost")
	private Float totalCost;

	@Column(name = "address")
	private String address;

	@Column(name = "status")
	private boolean status;

	@ManyToOne
	@JoinColumn(name = "customerid")
	// @JsonBackReference
	private PersonEntity customers;

	@JsonManagedReference
	@OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
	Set<OrderDetailEntity> orderDetails = new HashSet<OrderDetailEntity>();

	public OrderEntity() {
		super();
	}

	public OrderEntity(OrderDTO order) {
		super();
		this.id = order.getId();
		this.timebought = order.getTimebought();
		this.totalCost = order.getTotalCost();
		this.address = order.getAddress();
		this.status = order.isStatus();
		this.customers = new PersonEntity(order.getCustomers());
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public PersonEntity getCustomers() {
		return customers;
	}

	public void setCustomers(PersonEntity customers) {
		this.customers = customers;
	}

	public LocalDateTime getTimebought() {
		return timebought;
	}

	public void setTimebought(LocalDateTime timebought) {
		this.timebought = timebought;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Set<OrderDetailEntity> getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(Set<OrderDetailEntity> orderDetails) {
		this.orderDetails = orderDetails;
	}
//	public void setOrderDetails(Set<OrderDetailDTO> orderDetails) {
//
//		this.orderDetails = orderDetails.stream().map(detail -> new OrderDetailEntity(detail))
//				.collect(Collectors.toSet());
//	}

	public void setTotalCost(Float totalCost) {
		if (totalCost < 0) {
			throw new IllegalArgumentException("Total must not below zero");
		}
		this.totalCost = totalCost;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderEntity other = (OrderEntity) obj;
		return id == other.id;
	}

}
