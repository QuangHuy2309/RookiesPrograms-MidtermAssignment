package com.nashtech.MyBikeShop.entity;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.nashtech.MyBikeShop.DTO.ProductDTO;
import com.sun.istack.NotNull;

@Entity
@Table(name="products")
public class ProductEnity {
	@Id
	@NotNull
	@Column(name="id")
	private String id;
	
	@NotNull
	@Column(name="name")
	private String name;
	
	@NotNull
	@Column(name="price")
	private float price;
	
	@NotNull
	@Column(name="quantity")
	private int quantity;
	
	@NotNull
	@Column(name="productType")
	private String productType;
	
	@Column(name="description")
	private String description;
	
	@Column(name="brand")
	private String brand;
	
	@Column(name="photo1")
	private String photo1;
	
	@Column(name="photo2")
	private String photo2;
	
	@Column(name="photo3")
	private String photo3;
	
	@Column(name="photo4")
	private String photo4;
	
	@OneToMany(mappedBy = "products", fetch=FetchType.EAGER)
	private Collection<OrderEntity> orders;
	
	public ProductEnity() {
		super();
	}

	public ProductEnity(ProductDTO product) {
		super();
		this.id = product.getId();
		this.name = product.getName();
		this.price = product.getPrice();
		this.quantity = product.getQuantity();
		this.productType = product.getProductType();
		this.description = product.getProductType();
		this.brand = product.getBrand();
		this.photo1 = product.getPhoto1();
		this.photo2 = product.getPhoto2();
		this.photo3 = product.getPhoto3();
		this.photo4 = product.getPhoto4();
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		StringBuilder idTrim = new StringBuilder();
		idTrim.append(id.trim());
		if (idTrim.length() == 0) {
			throw new IllegalArgumentException("Name is invalid");
		}
		this.name = idTrim.toString();
		this.id = id;
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

	public void setQuantity(int quantity) {
		if (quantity < 0) {
			throw new IllegalArgumentException("Quantity is invalid");
		}
		this.quantity = quantity;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		StringBuilder typeTrim = new StringBuilder();
		typeTrim.append(productType.trim());
		if (typeTrim.length() == 0) {
			throw new IllegalArgumentException("Type of product is invalid");
		}
		this.productType = typeTrim.toString();
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

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		//return super.toString();
		return "ID: "+ this.id + "\t Name: " + this.name + "\t Price:" + this.price + "\t Quantity: " 
			+ this.quantity + "\t Type: " + this.productType + "\t Brand: " +this.brand;
	}

}
