package com.nashtech.MyBikeShop.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.nashtech.MyBikeShop.DTO.PersonDTO;

@Entity
@Table(name="persons")
public class PersonEntity {
	@Id
	private String email;
	
	@Column(name="password")
	private String password;
	
	@Column(name="fullname")
	private String fullname;
	
	@Column(name="dob")
	private Date dob;
	
	@Column(name="gender")
	private boolean gender;
	
	@Column(name="address")
	private String address;
	
	@Column(name="phonenumber")
	private String phonenumber;
	
	@Column(name="role")
	private String role;
	
	
	
	public PersonEntity() {
		super();
	}

	public PersonEntity(PersonDTO personDTO) {
		super();
		this.email = personDTO.getEmail();
		this.password = personDTO.getPassword();
		this.fullname = personDTO.getFullname();
		this.dob = personDTO.getDob();
		this.gender = personDTO.isGender();
		this.address = personDTO.getAddress();
		this.phonenumber = personDTO.getPhonenumber();
		this.role = personDTO.getRole();
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

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public boolean isGender() {
		return gender;
	}

	public void setGender(boolean gender) {
		this.gender = gender;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	
}
