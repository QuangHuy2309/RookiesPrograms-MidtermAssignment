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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.nashtech.MyBikeShop.DTO.CategoriesDTO;

@Entity
@Table(name = "categories")
public class CategoriesEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "description")
	private String description;
	
	@OneToMany(mappedBy = "categories", fetch = FetchType.EAGER)
//	@JsonManagedReference
	@JsonIgnore
	private Collection<ProductEntity> product;
	
	public CategoriesEntity() {
		super();
	}
	public CategoriesEntity(int id) {
		super();
		this.id = id;
	}
	public CategoriesEntity(CategoriesDTO categories) {
		this.id = categories.getId();
		this.name = categories.getName();
		this.description = categories.getDescription();
	}

	public CategoriesEntity(int id, String categoriesname, String description) {
		super();
		this.id = id;
		this.name = categoriesname;
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
		StringBuilder nameTrim = new StringBuilder();
		nameTrim.append(name.trim());
		if (nameTrim.length() == 0) {
			throw new IllegalArgumentException("Name is invalid");
		}
		this.name = nameTrim.toString();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
