package com.nashtech.MyBikeShop.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ReviewKey {
	@Column(name = "customerid")
	private String customerId;

	@Column(name = "productid")
	private String productId;
	
	public ReviewKey() {
		super();
	}

	public ReviewKey(String customerId, String productId) {
		super();
		this.customerId = customerId;
		this.productId = productId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	
	
}
