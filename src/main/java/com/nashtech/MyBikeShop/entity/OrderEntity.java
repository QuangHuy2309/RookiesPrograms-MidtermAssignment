package com.nashtech.MyBikeShop.entity;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.nashtech.MyBikeShop.DTO.OrderDTO;
import com.sun.istack.NotNull;

@Entity
@Table(name = "orderbill")
public class OrderEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "quantity")
	private int quantity;

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
	//@JsonBackReference
	private PersonEntity customers;
	
	@OneToMany(mappedBy = "order")
	Collection<OrderDetailEntity> orderDetails;
	
	public OrderEntity() {
		super();
	}

	public OrderEntity(OrderDTO order) {
		super();
		this.id = order.getId();
		this.quantity = order.getQuantity();
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

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		if (quantity < 0) {
			throw new IllegalArgumentException("Quantity must not below zero");
		}
		this.quantity = quantity;
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
