package com.nashtech.MyBikeShop.DTO;

public class OrderImportDetailDTO {
	private int orderImportId;
	private String productId;
	private int amount;
	private Float unitprice;
	
	public OrderImportDetailDTO() {
	}
	
	public OrderImportDetailDTO(int orderImportId, String productId, int amount, Float unitprice) {
		super();
		this.orderImportId = orderImportId;
		this.productId = productId;
		this.amount = amount;
		this.unitprice = unitprice;
	}
	public int getOrderImportId() {
		return orderImportId;
	}
	public void setOrderImportId(int orderImportId) {
		this.orderImportId = orderImportId;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}

	public Float getUnitprice() {
		return unitprice;
	}

	public void setUnitprice(Float unitprice) {
		this.unitprice = unitprice;
	}
	
}
