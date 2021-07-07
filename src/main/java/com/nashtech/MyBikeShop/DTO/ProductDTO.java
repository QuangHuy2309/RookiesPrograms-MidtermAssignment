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
	private String photo1;
	private String photo2;
	private String photo3;
	private String photo4;
	
	public ProductDTO() {
	}

	public ProductDTO(String id, String name, float price, int quantity, CategoriesDTO categories, String description,
			String brand, LocalDateTime createDate, LocalDateTime updateDate, String photo1, String photo2, String photo3,
			String photo4) {
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
		this.photo1 = photo1;
		this.photo2 = photo2;
		this.photo3 = photo3;
		this.photo4 = photo4;
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

	public String getPhoto1() {
		return photo1;
	}

	public void setPhoto1(String photo1) {
		this.photo1 = photo1;
	}

	public String getPhoto2() {
		return photo2;
	}

	public void setPhoto2(String photo2) {
		this.photo2 = photo2;
	}

	public String getPhoto3() {
		return photo3;
	}

	public void setPhoto3(String photo3) {
		this.photo3 = photo3;
	}

	public String getPhoto4() {
		return photo4;
	}

	public void setPhoto4(String photo4) {
		this.photo4 = photo4;
	}

}
