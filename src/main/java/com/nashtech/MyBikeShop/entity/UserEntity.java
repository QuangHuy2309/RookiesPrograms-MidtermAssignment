package com.nashtech.MyBikeShop.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "persons")
public class UserEntity {
	@Id
	@Column(name = "email")
	private String email;

	@Column(name = "password")
	private String password;

	@Column(name = "fullname")
	private String fullname;

	@Column(name = "role")
	private String role;

	public UserEntity() {
	}

	public UserEntity(String email, String password, String username, String role) {
		super();
		this.email = email;
		this.password = password;
		this.fullname = username;
		this.role = role;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		StringBuilder roleTrim = new StringBuilder();
		roleTrim.append(role.trim());
		if (roleTrim.length() == 0) {
			throw new IllegalArgumentException("Role is invalid");
		}
		this.role = roleTrim.toString();
	}

}
