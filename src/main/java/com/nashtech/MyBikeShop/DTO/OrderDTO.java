package com.nashtech.MyBikeShop.DTO;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

import com.nashtech.MyBikeShop.entity.PersonEntity;
import com.nashtech.MyBikeShop.entity.ProductEntity;

public class OrderDTO {
	private int id;
	private LocalDateTime timebought;
	private float totalCost;
	private String address;
	private boolean status;
	private PersonDTO customers;
	private Set<OrderDetailDTO> orderDetails;

	public OrderDTO() {
	}
	
	public OrderDTO(float totalCost, String address, boolean status, PersonDTO customers) {
		super();
		this.id = id;
		this.totalCost = totalCost;
		this.status = status;
		this.customers = customers;
	}

	public OrderDTO(int id, LocalDateTime timebought, float totalCost, String address, boolean status,
			PersonDTO customers, Set<OrderDetailDTO> orderdetail) {
		super();
		this.id = id;
		this.timebought = timebought;
		this.totalCost = totalCost;
		this.address = address;
		this.status = status;
		this.customers = customers;
		this.orderDetails = orderdetail;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public PersonDTO getCustomers() {
		return customers;
	}

	public void setCustomers(PersonDTO customers) {
		this.customers = customers;
	}

	public Set<OrderDetailDTO> getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(Set<OrderDetailDTO> orderDetails) {
		this.orderDetails = orderDetails;
	}

	@Override
	public int hashCode() {
		return Objects.hash(address, customers, id, orderDetails, status, timebought, totalCost);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderDTO other = (OrderDTO) obj;
		return Objects.equals(address, other.address) && Objects.equals(customers, other.customers) && id == other.id
				&& Objects.equals(orderDetails, other.orderDetails) && status == other.status
				&& Objects.equals(timebought, other.timebought)
				&& Float.floatToIntBits(totalCost) == Float.floatToIntBits(other.totalCost);
	}

	

}
