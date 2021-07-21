package com.nashtech.MyBikeShop.entity;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.nashtech.MyBikeShop.DTO.PersonDTO;

//@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class)
@Entity
@Table(name="persons")
public class PersonEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="email")
	private String email;
	
	@Column(name="password")
	private String password;
	
	@Column(name="fullname")
	private String fullname;
	
//	@DateTimeFormat(pattern="yyyy/MM/dd")
	@JsonFormat(pattern="yyyy-MM-dd")
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
	
	@OneToMany(mappedBy = "customers", fetch=FetchType.EAGER)
	@JsonIgnore
//	@JsonManagedReference
	private Set<OrderEntity> orders;
	
	@OneToMany(mappedBy = "customer", fetch=FetchType.EAGER)
//	@JsonManagedReference
	@JsonIgnore
	Set<RateEntity> reviews;
	
	public PersonEntity() {
		super();
	}
	
	public PersonEntity(int id, String email, String password, String fullname, String role) {
		super();
		this.id = id;
		this.email = email;
		this.password = password;
		this.fullname = fullname;
		this.role = role;
	}

	public PersonEntity(PersonDTO personDTO) {
		super();
		this.id = personDTO.getId();
		this.email = personDTO.getEmail();
		this.password = personDTO.getPassword();
		this.fullname = personDTO.getFullname();
		this.dob = personDTO.getDob();
		this.gender = personDTO.isGender();
		this.address = personDTO.getAddress();
		this.phonenumber = personDTO.getPhonenumber();
		this.role = personDTO.getRole();
	}
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

//	@Override
//	public int hashCode() {
//		return Objects.hash(address, dob, email, fullname, gender, id, orders, password, phonenumber, reviews, role);
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		PersonEntity other = (PersonEntity) obj;
//		return Objects.equals(address, other.address) && Objects.equals(dob, other.dob)
//				&& Objects.equals(email, other.email) && Objects.equals(fullname, other.fullname)
//				&& gender == other.gender && id == other.id && Objects.equals(orders, other.orders)
//				&& Objects.equals(password, other.password) && Objects.equals(phonenumber, other.phonenumber)
//				&& Objects.equals(reviews, other.reviews) && Objects.equals(role, other.role);
//	}

//	public Collection<OrderEntity> getOrdersHadBought() {
//		return orders;
//	}
//
//	public void setOrders(Collection<OrderEntity> orders) {
//		this.orders = orders;
//	}
	
}
