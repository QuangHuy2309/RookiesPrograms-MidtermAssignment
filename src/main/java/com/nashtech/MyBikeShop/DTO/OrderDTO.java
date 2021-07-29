package com.nashtech.MyBikeShop.DTO;

import java.util.List;
import java.util.Objects;

public class OrderDTO {
	private int id;
	private float totalCost;
	private String address;
	private boolean status;
	private String customersEmail;
	private List<OrderDetailDTO> orderDetails;

	public OrderDTO() {
	}
	
	public OrderDTO(float totalCost, String address, boolean status, String customersEmail) {
		super();
		this.totalCost = totalCost;
		this.status = status;
		this.customersEmail = customersEmail;
	}

	public OrderDTO(int id, float totalCost, String address, boolean status,
			String customersEmail, List<OrderDetailDTO> orderdetail) {
		super();
		this.id = id;
		this.totalCost = totalCost;
		this.address = address;
		this.status = status;
		this.customersEmail = customersEmail;
		this.orderDetails = orderdetail;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getCustomersEmail() {
		return customersEmail;
	}

	public void setCustomersEmail(String customersEmail) {
		this.customersEmail = customersEmail;
	}

	public List<OrderDetailDTO> getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(List<OrderDetailDTO> orderDetails) {
		this.orderDetails = orderDetails;
	}

	@Override
	public int hashCode() {
		return Objects.hash(address, id, orderDetails, status, totalCost);
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
		return Objects.equals(address, other.address) && id == other.id
				&& Objects.equals(orderDetails, other.orderDetails) && status == other.status
				&& Float.floatToIntBits(totalCost) == Float.floatToIntBits(other.totalCost);
	}
}
