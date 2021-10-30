package com.nashtech.MyBikeShop.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.nashtech.MyBikeShop.DTO.OrderDetailDTO;

@Entity
@Table(name = "orderdetails")
public class OrderDetailEntity {
	@EmbeddedId
	private OrderDetailsKey id;

	@Column(name = "amount")
	private int ammount;
	
	@Column(name = "unitprice")
	private double unitPrice;

	@ManyToOne
	@JoinColumn(name = "orderid", insertable = false, updatable = false)
	@JsonBackReference
	private OrderEntity order;

	@ManyToOne
	@JsonBackReference
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

		public OrderDetailsKey(int orderId, String productId) {
			super();
			this.orderId = orderId;
			this.productId = productId;
		}

	}

	public OrderDetailEntity() {
	}

	public OrderDetailEntity(OrderDetailsKey id, int ammount, double unitPrice) {
		super();
		this.id = id;
		this.ammount = ammount;
		this.unitPrice = unitPrice;
	}

	public OrderDetailEntity(OrderDetailsKey id, int ammount, double unitPrice, OrderEntity order, ProductEntity product) {
		super();
		this.id = id;
		this.ammount = ammount;
		this.order = order;
		this.product = product;
		this.unitPrice = unitPrice;
	}

	public OrderDetailEntity(OrderDetailDTO orderDTO) {
		super();
		this.ammount = orderDTO.getAmmount();
		this.unitPrice = orderDTO.getUnitPrice();
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

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

}
