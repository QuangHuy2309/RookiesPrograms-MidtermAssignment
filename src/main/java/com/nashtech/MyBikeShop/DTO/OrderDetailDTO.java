package com.nashtech.MyBikeShop.DTO;

public class OrderDetailDTO {
	private int orderId;
	private String productId;
	private int ammount;
	private double unitPrice;

	public OrderDetailDTO() {
		super();
	}

	public OrderDetailDTO(int orderId, String productId, int ammount, double unitPrice) {
		super();
		this.orderId = orderId;
		this.productId = productId;
		this.ammount = ammount;
		this.unitPrice = unitPrice;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public int getAmmount() {
		return ammount;
	}

	public void setAmmount(int ammount) {
		this.ammount = ammount;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

}
