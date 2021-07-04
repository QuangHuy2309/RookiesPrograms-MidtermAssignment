package com.nashtech.MyBikeShop.entity;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.nashtech.MyBikeShop.DTO.CategoriesDTO;

@Entity
@Table(name = "categories")
public class CategoriesEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@NotNull
	@Column(name = "categoriesname")
	private String categoriesName;
	
	@Column(name = "description")
	private String description;
	
	@OneToMany(mappedBy = "categories", fetch = FetchType.EAGER)
	private Collection<ProductEntity> product;
	
	public CategoriesEntity() {
		super();
	}

	public CategoriesEntity(CategoriesDTO categories) {
		this.id = categories.getId();
		this.categoriesName = categories.getCategoriesName();
		this.description = categories.getDescription();
	}

	public CategoriesEntity(int id, @NotNull String categoriesname, String description) {
		super();
		this.id = id;
		this.categoriesName = categoriesname;
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
