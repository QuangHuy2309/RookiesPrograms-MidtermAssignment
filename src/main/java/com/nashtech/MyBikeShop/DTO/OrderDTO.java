package com.nashtech.MyBikeShop.DTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;

public class OrderDTO {
	private int id;
	private Double totalCost;
	private String address;
	private Integer status;
	private String customersEmail;
	private String customersName;
	private String employeeApprovedName;
	private LocalDateTime timebought;
	private boolean payment;
	private String note;
	private List<OrderDetailDTO> orderDetails;

	public OrderDTO() {
	}
	
	public OrderDTO(String address, int status, String customersEmail) {
		super();
//		this.totalCost = totalCost;
		this.status = status;
		this.customersEmail = customersEmail;
	}

	public OrderDTO(int id, String address, int status,
			String customersEmail, List<OrderDetailDTO> orderdetail) {
		super();
		this.id = id;
		this.address = address;
		this.status = status;
		this.customersEmail = customersEmail;
		this.orderDetails = orderdetail;
	}

	public OrderDTO(int id, Double totalCost, String address, Integer status, String customersEmail,
			String customersName, LocalDateTime timebought, List<OrderDetailDTO> orderDetails) {
		super();
		this.id = id;
		this.totalCost = totalCost;
		this.address = address;
		this.status = status;
		this.customersEmail = customersEmail;
		this.customersName = customersName;
		this.timebought = timebought;
		this.orderDetails = orderDetails;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Double getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(Double totalCost) {
		this.totalCost = totalCost;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int isStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getCustomersEmail() {
		return customersEmail;
	}

	public void setCustomersEmail(String customersEmail) {
		this.customersEmail = customersEmail;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCustomersName() {
		return customersName;
	}

	public void setCustomersName(String customersName) {
		this.customersName = customersName;
	}

	public LocalDateTime getTimebought() {
		return timebought;
	}

	public void setTimebought(LocalDateTime timebought) {
		this.timebought = timebought;
	}

	public boolean isPayment() {
		return payment;
	}

	public void setPayment(boolean payment) {
		this.payment = payment;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getEmployeeApprovedName() {
		return employeeApprovedName;
	}

	public void setEmployeeApprovedName(String employeeApprovedName) {
		this.employeeApprovedName = employeeApprovedName;
	}

	public List<OrderDetailDTO> getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(List<OrderDetailDTO> orderDetails) {
		this.orderDetails = orderDetails;
	}

	@Override
	public int hashCode() {
		return Objects.hash(address, id, orderDetails, status);
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
				&& Objects.equals(orderDetails, other.orderDetails) && status == other.status;
	}
}
