package com.nashtech.MyBikeShop.DTO;

import java.time.LocalDateTime;
import java.util.Date;

public class ProductDTO {
	private String id;
	private String name;
	private float price;
	private int quantity;
	private CategoriesDTO categories;
	private String description;
	private String brand;
	private LocalDateTime createDate;
	private LocalDateTime updateDate;
	private String photo;

	
	public ProductDTO() {
	}

	public ProductDTO(String id, String name, float price, int quantity, CategoriesDTO categories, String description,
			String brand, LocalDateTime createDate, LocalDateTime updateDate, String photo) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.quantity = quantity;
		this.categories = categories;
		this.description = description;
		this.brand = brand;
		this.createDate = createDate;
		this.updateDate = updateDate;
		this.photo = photo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		StringBuilder idTrim = new StringBuilder();
		idTrim.append(id.trim());
		if (idTrim.length() == 0) {
			throw new IllegalArgumentException("ID is invalid");
		}
		this.id = idTrim.toString();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		StringBuilder nameTrim = new StringBuilder();
		nameTrim.append(name.trim());
		if (nameTrim.length() == 0) {
			throw new IllegalArgumentException("Name is invalid");
		}
		this.name = nameTrim.toString();
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

	public CategoriesDTO getCategories() {
		return categories;
	}

	public void setCategories(CategoriesDTO categories) {
		this.categories = categories;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public LocalDateTime getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}

	public LocalDateTime getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(LocalDateTime updateDate) {
		this.updateDate = updateDate;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	
}
