package com.nashtech.MyBikeShop.DTO;

import java.time.LocalDateTime;
import java.util.Date;


import com.nashtech.MyBikeShop.entity.PersonEntity;
import com.nashtech.MyBikeShop.entity.ProductEntity;

public class OrderDTO {
	private int id;
	private int quantity;
	private LocalDateTime timebought;
	private float totalCost;
	private String address;
	private int rateNum;
	private String rateText;
	private boolean status;
	private ProductDTO products;
	private PersonDTO customers;
	
	public OrderDTO() {}

	public OrderDTO(int id, int quantity, LocalDateTime timebought, float totalCost, String address, int rateNum,
			String rateText, boolean status, ProductDTO products, PersonDTO customers) {
		super();
		this.id = id;
		this.quantity = quantity;
		this.timebought = timebought;
		this.totalCost = totalCost;
		this.address = address;
		this.rateNum = rateNum;
		this.rateText = rateText;
		this.status = status;
		this.products = products;
		this.customers = customers;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
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
		this.totalCost = totalCost;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getRateNum() {
		return rateNum;
	}

	public void setRateNum(int rateNum) {
		this.rateNum = rateNum;
	}

	public String getRateText() {
		return rateText;
	}

	public void setRateText(String rateText) {
		this.rateText = rateText;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public ProductDTO getProducts() {
		return products;
	}

	public void setProducts(ProductDTO products) {
		this.products = products;
	}

	public PersonDTO getCustomers() {
		return customers;
	}

	public void setCustomers(PersonDTO customers) {
		this.customers = customers;
	}
	
}
