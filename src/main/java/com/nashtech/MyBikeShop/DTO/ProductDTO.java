package com.nashtech.MyBikeShop.DTO;

import java.time.LocalDateTime;

import javax.persistence.Lob;

import org.hibernate.annotations.Type;

public class ProductDTO {
	private String id;
	private String name;
	private float price;
	private int quantity;
	private int categoriesId;
	private String description;
	private String brand;
	private LocalDateTime createDate;
	private LocalDateTime updateDate;
	private String nameEmployeeUpdate;
	@Lob
	@Type(type = "org.hibernate.type.BinaryType")
	private byte[] photo;

	public ProductDTO() {
	}

	public ProductDTO(String id, String name, float price, int quantity, int categoriesId, String nameEmployeeUpdate) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.quantity = quantity;
		this.categoriesId = categoriesId;
		this.nameEmployeeUpdate = nameEmployeeUpdate;
	}

	public ProductDTO(String id, String name, float price, int quantity, int categories, String description,
			LocalDateTime createDate, String brand, byte[] photo) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.quantity = quantity;
		this.categoriesId = categories;
		this.description = description;
		this.brand = brand;
		this.createDate = createDate;
		this.photo = photo;
	}
	
	public ProductDTO(String id, String name, float price, int quantity, int categoriesId, String description,
			String brand, LocalDateTime createDate, LocalDateTime updateDate, String nameEmployeeUpdate, byte[] photo) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.quantity = quantity;
		this.categoriesId = categoriesId;
		this.description = description;
		this.brand = brand;
		this.createDate = createDate;
		this.updateDate = updateDate;
		this.nameEmployeeUpdate = nameEmployeeUpdate;
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

	public int getCategoriesId() {
		return categoriesId;
	}

	public void setCategoriesId(int categoriesId) {
		this.categoriesId = categoriesId;
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

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public LocalDateTime getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}

	public String getNameEmployeeUpdate() {
		return nameEmployeeUpdate;
	}

	public void setNameEmployeeUpdate(String nameEmployeeUpdate) {
		this.nameEmployeeUpdate = nameEmployeeUpdate;
	}

	public LocalDateTime getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(LocalDateTime updateDate) {
		this.updateDate = updateDate;
	}
	
}
