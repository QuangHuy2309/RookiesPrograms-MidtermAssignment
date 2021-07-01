package com.nashtech.MyBikeShop.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="orderbill")
public class OrderEntity {
	@Id
	@GeneratedValue
	@Column(name="id")
	private int id;
	
	@Column(name="quantity")
	private int quantity;
	
	@Column(name="timebought")
	private Date timebought;
	
	@Column(name="totalCost")
	private float totalCost;
	
	@Column(name="address")
	private String address;
	
	@Column(name="rateNum")
	private int rateNum;
	
	@Column(name="rateText")
	private String rateText;
	
	@Column(name="status")
	private boolean status;
	
	@ManyToOne
	@JoinColumn(name="productId")
	private ProductEnity products;
	
	@ManyToOne
	@JoinColumn(name="customerEmail")
	private PersonEntity customers;
	
	public OrderEntity() {
		super();
	}

	

	public OrderEntity(int id, int quantity, Date timebought, float totalCost, String address, int rateNum,
			String rateText, boolean status, ProductEnity products, PersonEntity customers) {
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

	

	public ProductEnity getProducts() {
		return products;
	}



	public void setProducts(ProductEnity products) {
		this.products = products;
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
		this.quantity = quantity;
	}

	public Date getTimebought() {
		return timebought;
	}

	public void setTimebought(Date timebought) {
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
	
	
}
