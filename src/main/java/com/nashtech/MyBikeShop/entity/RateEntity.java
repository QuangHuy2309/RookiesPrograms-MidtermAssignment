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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.nashtech.MyBikeShop.DTO.RateDTO;

@Entity
@Table(name="review")
public class RateEntity {
	@EmbeddedId
	private RateKey id;
	
//	@JsonBackReference
	@ManyToOne
    @JoinColumn(name = "customerid", insertable = false, updatable = false)
	private PersonEntity customer;
	
//	@JsonBackReference
	@ManyToOne
    @JoinColumn(name = "productid", insertable = false, updatable = false)
    private ProductEntity product;
	
	@Column(name = "rate_num")
	private int rateNum;
	
	@Column(name = "rate_text")
	private String rateText;
	
	@Column(name = "datereview")
	private Date dateReview;

	@Embeddable
	public static class RateKey implements Serializable {
		private static final long serialVersionUID = -6908742347311635007L;

		@Column(name = "customerid")
		private int customerId;

		@Column(name = "productid")
		private String productId;
		
		public RateKey() {
			super();
		}

		public RateKey(int customerId, String productId) {
			super();
			this.customerId = customerId;
			this.productId = productId;
		}

		public int getCustomerId() {
			return customerId;
		}

		public void setCustomerId(int customerId) {
			this.customerId = customerId;
		}

		public String getProductId() {
			return productId;
		}

		public void setProductId(String productId) {
			this.productId = productId;
		}
	}

	
	public RateEntity() {
		super();
	}
	
	public RateEntity(RateKey id, PersonEntity customer, ProductEntity product, int rateNum, String rateText,
			Date datereview) {
		super();
		this.id = id;
		this.customer = customer;
		this.product = product;
		this.rateNum = rateNum;
		this.rateText = rateText;
		this.dateReview = datereview;
	}

	public RateEntity(RateDTO rateDTO) {
		super();
//		this.id.customerId = rateDTO.getCustomerId();
//		this.id.productId = rateDTO.getProductId();
		this.id = new RateKey(rateDTO.getCustomerId(), rateDTO.getProductId());
		//this.id.setProductId(rateDTO.getProductId());
		this.rateNum = rateDTO.getRateNum();
		this.rateText = rateDTO.getRateText();
	}

	public RateKey getId() {
		return id;
	}

	public void setId(RateKey id) {
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

	public Date getDateReview() {
		return dateReview;
	}

	public void setDateReview(Date dateReview) {
		this.dateReview = dateReview;
	}
	
	
}
