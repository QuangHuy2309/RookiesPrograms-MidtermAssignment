package com.nashtech.MyBikeShop.entity;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.nashtech.MyBikeShop.DTO.ProductDTO;

@Entity
@Table(name = "products")
public class ProductEntity {
	@Id
	@NotNull
	@Column(name = "id")
	private String id;

	@NotNull
	@Column(name = "name")
	private String name;

	@NotNull
	@Column(name = "price")
	private float price;

	@NotNull
	@Column(name = "quantity")
	private int quantity;

	@Column(name = "description")
	private String description;

	@Column(name = "brand")
	private String brand;

	@Column(name = "createdate")
	private LocalDateTime createDate;

	@Column(name = "updatedate")
	private LocalDateTime updateDate;

	@Column(name = "photo")
	private String photo;

	@OneToMany(mappedBy = "product")
	Collection<OrderDetailEntity> orderDetails;
	
	@OneToMany(mappedBy = "product")
	Collection<ReviewEntity> reviews;
	
	@ManyToOne
	@JoinColumn(name = "producttype")
	private CategoriesEntity categories;

	public ProductEntity() {
		super();
	}

	public ProductEntity(ProductDTO product) {
		super();
		this.id = product.getId();
		this.name = product.getName();
		this.price = product.getPrice();
		this.quantity = product.getQuantity();
		this.categories = new CategoriesEntity(product.getCategories());
		this.description = product.getDescription();
		this.brand = product.getBrand();
		this.createDate = product.getCreateDate();
		this.updateDate = product.getUpdateDate();
		this.photo = product.getPhoto();
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
		if (price < 0) {
			throw new IllegalArgumentException("Price is invalid");
		}
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void changeQuantity(int numChange) {
		boolean checkNumberChange = ((numChange < 0) && (this.quantity < Math.abs(numChange)));
		if (checkNumberChange)
			throw new IllegalArgumentException("The number of quantity change is invalid");
		this.quantity += numChange;
	}

	public void setQuantity(int quantity) {
		if (quantity < 0) {
			throw new IllegalArgumentException("Quantity is invalid");
		}
		this.quantity = quantity;
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

	public CategoriesEntity getCategories() {
		return categories;
	}

	public void setCategories(CategoriesEntity categories) {
		this.categories = categories;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		// return super.toString();
		return "ID: " + this.id + "\t Name: " + this.name + "\t Price:" + this.price + "\t Quantity: " + this.quantity
				+ "\t Type: " + this.categories.getName() + "\t Brand: " + this.brand;
	}

}
