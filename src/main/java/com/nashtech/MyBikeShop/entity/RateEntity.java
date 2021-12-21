package com.nashtech.MyBikeShop.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.nashtech.MyBikeShop.DTO.RateDTO;

@Entity
@Table(name = "reviewdetails")
public class RateEntity {
//	@EmbeddedId
//	private RateKey id;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@ManyToOne
	@JoinColumn(name = "customerid")
	private PersonEntity customer;

	@ManyToOne
	@JoinColumn(name = "productid")
	private ProductEntity product;

	@Column(name = "rate_num")
	private int rateNum;

	@Column(name = "rate_text")
	private String rateText;

	@Column(name = "datereview")
	private Date dateReview;

//	@Embeddable
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

		@Override
		public String toString() {
			return "RateKey [customerId=" + customerId + ", productId=" + productId + "]";
		}
		
	}

	public RateEntity() {
		super();
	}

	public RateEntity(int id, int rateNum, String rateText) {
		super();
		this.id = id;
		this.rateNum = rateNum;
		this.rateText = rateText;
	}

	public RateEntity(int id, PersonEntity customer, ProductEntity product, int rateNum, String rateText,
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
//		this.id = rateDTO.getId();
		this.rateNum = rateDTO.getRateNum();
		this.rateText = rateDTO.getRateText();
	}

//	public RateKey getId() {
//		return id;
//	}
//
//	public void setId(RateKey id) {
//		this.id = id;
//	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
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
