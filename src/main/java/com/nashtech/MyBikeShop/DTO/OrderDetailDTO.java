package com.nashtech.MyBikeShop.DTO;

import com.nashtech.MyBikeShop.entity.OrderEntity;
import com.nashtech.MyBikeShop.entity.ProductEntity;
import com.nashtech.MyBikeShop.entity.OrderDetailEntity.OrderDetailsKey;

public class OrderDetailDTO {
	private OrderDetailsKey id;

	private int ammount;
	private OrderEntity order;
	private ProductEntity product;

	public OrderDetailDTO() {
		super();
	}

	public OrderDetailDTO(OrderDetailsKey id, int ammount) {
		super();
		this.id = id;
		this.ammount = ammount;
	}

	public OrderDetailDTO(OrderDetailsKey id, int ammount, OrderEntity order, ProductEntity product) {
		super();
		this.id = id;
		this.ammount = ammount;
		this.order = order;
		this.product = product;
	}

	public OrderDetailsKey getId() {
		return id;
	}

	public void setId(OrderDetailsKey id) {
		this.id = id;
	}

	public int getAmmount() {
		return ammount;
	}

	public void setAmmount(int ammount) {
		this.ammount = ammount;
	}

	public OrderEntity getOrder() {
		return order;
	}

	public void setOrder(OrderEntity order) {
		this.order = order;
	}

	public ProductEntity getProduct() {
		return product;
	}

	public void setProduct(ProductEntity product) {
		this.product = product;
	}
	
}
