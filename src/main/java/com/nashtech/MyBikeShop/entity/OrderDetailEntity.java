package com.nashtech.MyBikeShop.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
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
	
	@Column(name = "amount")
	private int ammount;
	
	@ManyToOne
    @JoinColumn(name = "orderid", insertable = false, updatable = false)
    private OrderEntity order;
	
	@ManyToOne
    @JoinColumn(name = "productid", insertable = false, updatable = false)
    private ProductEntity product;
		
	
	@Embeddable
	public static class OrderDetailsKey implements Serializable {

		private static final long serialVersionUID = -4415409401138657585L;

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
