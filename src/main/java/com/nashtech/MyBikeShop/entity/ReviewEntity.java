package com.nashtech.MyBikeShop.entity;

import java.util.Date;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Entity
@Table(name="review")
public class ReviewEntity {
	@EmbeddedId
	private ReviewKey id;
	
	@ManyToOne
    @MapsId("customerId")
    @JoinColumn(name = "customerid")
	private PersonEntity customer;
	
	@ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "productid")
    private ProductEntity product;
	
	private int rateNum;
	
	private String rateText;
	
	private Date datereview;

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
