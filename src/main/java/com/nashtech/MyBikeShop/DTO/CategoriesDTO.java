package com.nashtech.MyBikeShop.DTO;

public class CategoriesDTO {
	
	private int id;

	private String categoriesName;

	private String description;

	
	
	public CategoriesDTO() {
		super();
	}

	
	
	public CategoriesDTO(int id, String categoriesName, String description) {
		super();
		this.id = id;
		this.categoriesName = categoriesName;
		this.description = description;
	}



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCategoriesName() {
		return categoriesName;
	}

	public void setCategoriesName(String categoriesName) {
		this.categoriesName = categoriesName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
