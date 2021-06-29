package com.nashtech.MyBikeShop.entity;

public class Product {
	private String id;
	private String name;
	private float price;
	private int quantity;
	private String productType;
	private String description;
	private String brand;
	private String photo1;
	private String photo2;
	private String photo3;
	private String photo4;

	public Product() {
		super();
	}

	public Product(String id, String name, float price, int quantity, String product_type, String description,
			String brand) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.quantity = quantity;
		this.productType = product_type;
		this.description = description;
		this.brand = brand;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		StringBuilder id_trim = new StringBuilder();
		id_trim.append(id.trim());
		if (id_trim.length() == 0) {
			throw new IllegalArgumentException("Name is invalid");
		}
		this.name = id_trim.toString();
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		StringBuilder name_trim = new StringBuilder();
		name_trim.append(name.trim());
		if (name_trim.length() == 0) {
			throw new IllegalArgumentException("Name is invalid");
		}
		this.name = name_trim.toString();
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
		StringBuilder type_trim = new StringBuilder();
		type_trim.append(productType.trim());
		if (type_trim.length() == 0) {
			throw new IllegalArgumentException("Type of product is invalid");
		}
		this.productType = type_trim.toString();
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
