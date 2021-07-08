package com.nashtech.MyBikeShop.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="review")
public class ReviewEntity {
	@EmbeddedId
	private ReviewKey id;
	
	@ManyToOne
    @JoinColumn(name = "customerid", insertable = false, updatable = false)
	private PersonEntity customer;
	
	@ManyToOne
    @JoinColumn(name = "productid", insertable = false, updatable = false)
    private ProductEntity product;
	
	@Column(name = "rate_text")
	private int rateNum;
	
	@Column(name = "rate_num")
	private String rateText;
	
	@Column(name = "datereview")
	private Date datereview;

	@Embeddable
	public static class ReviewKey implements Serializable {
		private static final long serialVersionUID = -6908742347311635007L;

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

	
	public ReviewEntity() {
		super();
	}
	
	public ReviewEntity(ReviewKey id, PersonEntity customer, ProductEntity product, int rateNum, String rateText,
			Date datereview) {
		super();
		this.id = id;
		this.customer = customer;
		this.product = product;
		this.rateNum = rateNum;
		this.rateText = rateText;
		this.datereview = datereview;
	}



	public ReviewKey getId() {
		return id;
	}

	public void setId(ReviewKey id) {
		this.id = id;
	}

	public PersonEntity getCustomer() {
		return customer;
	}

	public void setCustomer(PersonEntity customer) {
		this.customer = customer;
	}

	public ProductEntity getProduct() {
		return product;
	}

	public void setProduct(ProductEntity product) {
		this.product = product;
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

	public Date getDatereview() {
		return datereview;
	}

	public void setDatereview(Date datereview) {
		this.datereview = datereview;
	}
	
	
}
