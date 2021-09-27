package com.nashtech.MyBikeShop.DTO;

import javax.persistence.Lob;

import org.hibernate.annotations.Type;

public class ReportTopProduct {
	private String id;
	private String name;
	private float price;
	private int quantity;
	private long totalsold;
	@Lob
	@Type(type = "org.hibernate.type.BinaryType")
	private byte[] photo;

	public ReportTopProduct() {
	}

	public ReportTopProduct(String id, String name, float price, int quantity, byte[] photo, long totalsold) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.quantity = quantity;
		this.photo = photo;
		this.totalsold = totalsold;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public long getTotalsold() {
		return totalsold;
	}

	public void setTotalsold(long totalsold) {
		this.totalsold = totalsold;
	}

}
