package com.nashtech.MyBikeShop.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Entity
@Table(name="orderdetails")
public class OrderDetailEntity {
	@EmbeddedId
	private OrderDetailsKey id;
	
	@ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "orderid")
    private OrderEntity order;
	
	@ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "productid")
    private ProductEntity product;
	
	private int ammount;

	public OrderDetailsKey getId() {
		return id;
	}

	public void setId(OrderDetailsKey id) {
		this.id = id;
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

	public int getAmmount() {
		return ammount;
	}

	public void setAmmount(int ammount) {
		this.ammount = ammount;
	}
	
	
}
