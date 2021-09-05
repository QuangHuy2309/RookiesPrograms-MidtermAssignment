package com.nashtech.MyBikeShop.entity;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nashtech.MyBikeShop.DTO.ProductDTO;
import com.nashtech.MyBikeShop.exception.ObjectPropertiesIllegalException;

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
	@DecimalMin(value = "0", message = "Price must be not under 0")
	@Column(name = "price")
	private float price;

	@NotNull
	@DecimalMin(value = "0", message = "Quantity must be not under 0")
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

	@Lob
	@Type(type = "org.hibernate.type.BinaryType")
	@Column(name = "photo")
	private byte[] photo;

	@JsonIgnore
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	Set<OrderDetailEntity> orderDetails;
	
	@JsonIgnore
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	Set<OrderImportDetailEntity> orderImportDetails;

	@JsonIgnore
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	Set<RateEntity> reviews;

	@ManyToOne
	@JoinColumn(name = "producttype")
	private CategoriesEntity categories;

	public ProductEntity() {
		super();
	}

	public ProductEntity(@NotNull String id, @NotNull String name, @NotNull float price, @NotNull int quantity,
			CategoriesEntity categories) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.quantity = quantity;
		this.categories = categories;
	}

	public ProductEntity(ProductDTO product, CategoriesEntity cate) {
		super();
		this.id = product.getId();
		this.name = product.getName();
		this.price = product.getPrice();
		this.quantity = product.getQuantity();
		this.categories = cate;
		this.createDate = product.getCreateDate();
		this.description = product.getDescription();
		this.photo = product.getPhoto();
		this.brand = product.getBrand();
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
			throw new ObjectPropertiesIllegalException("The number of quantity change is invalid");
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

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public CategoriesEntity getCategories() {
		return categories;
	}

	public void setCategories(CategoriesEntity categories) {
		this.categories = categories;
	}

	public Set<RateEntity> getReviews() {
		return reviews;
	}

	public void setReviews(Set<RateEntity> reviews) {
		this.reviews = reviews;
	}
	
	
	
	public Set<OrderDetailEntity> getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(Set<OrderDetailEntity> orderDetails) {
		this.orderDetails = orderDetails;
	}

	public Set<OrderImportDetailEntity> getOrderImportDetails() {
		return orderImportDetails;
	}

	public void setOrderImportDetails(Set<OrderImportDetailEntity> orderImportDetails) {
		this.orderImportDetails = orderImportDetails;
	}

	@Override
	public String toString() {
		return "ID: " + this.id + "\t Name: " + this.name + "\t Price:" + this.price + "\t Quantity: " + this.quantity
				+ "\t Type: " + this.categories.getName() + "\t Brand: " + this.brand;
	}

	@Override
	public int hashCode() {
		return Objects.hash(brand, categories, createDate, description, id, name, orderDetails, price, quantity,
				reviews, updateDate);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductEntity other = (ProductEntity) obj;
		return Objects.equals(id, other.id) && Objects.equals(name, other.name)
				&& Objects.equals(orderDetails, other.orderDetails)
				&& Float.floatToIntBits(price) == Float.floatToIntBits(other.price) && quantity == other.quantity
				&& Objects.equals(reviews, other.reviews) && Objects.equals(updateDate, other.updateDate);
	}

}
