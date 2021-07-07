package com.nashtech.MyBikeShop.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class OrderDetailsKey {
	@Column(name = "orderid")
	private int orderId;

	@Column(name = "productid")
	private String productId;

	public OrderDetailsKey() {
		super();
	}

	public OrderDetailsKey(int orderId, String productID) {
		super();
		this.orderId = orderId;
		this.productId = productID;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public String getProductID() {
		return productId;
	}

	public void setProductID(String productID) {
		this.productId = productID;
	}

}
