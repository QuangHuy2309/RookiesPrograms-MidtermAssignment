package com.nashtech.MyBikeShop.DTO;

public class CategoriesDTO {
	
	private int id;

	private String name;

	private String description;

	
	
	public CategoriesDTO() {
		super();
	}

	
	
	public CategoriesDTO(int id, String categoriesName, String description) {
		super();
		this.id = id;
		this.name = categoriesName;
		this.description = description;
	}



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	

	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
