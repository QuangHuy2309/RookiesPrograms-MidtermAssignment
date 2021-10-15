package com.nashtech.MyBikeShop.DTO;

import java.util.Date;
import java.util.Objects;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class PersonDTO {
	private int id;
	@NotBlank
	@Size(max = 50)
	@Email
	private String email;
	@Size(min = 6, max = 40)
	private String password;
	@NotBlank
	// @Size(min = 3, max = 20)
	private String fullname;
	private Date dob;
	private boolean gender;
	private String address;
	private String phonenumber;
	private String role;
	private boolean status;

	PersonDTO() {
	}

	public PersonDTO(int id, @NotBlank @Size(max = 50) @Email String email, @Size(min = 6, max = 40) String password,
			@NotBlank String fullname, String role) {
		super();
		this.id = id;
		this.email = email;
		this.password = password;
		this.fullname = fullname;
		this.role = role;
	}

	public PersonDTO(int id, @NotBlank @Size(max = 50) @Email String email,
			@NotBlank @Size(min = 6, max = 40) String password, @NotBlank String fullname, @NotBlank String role,
			boolean status) {
		super();
		this.id = id;
		this.email = email;
		this.password = password;
		this.fullname = fullname;
		this.role = role;
		this.status = status;
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

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	@Override
	public int hashCode() {
		return Objects.hash(address, dob, email, fullname, gender, id, password, phonenumber, role);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PersonDTO other = (PersonDTO) obj;
		return Objects.equals(address, other.address) && Objects.equals(dob, other.dob)
				&& Objects.equals(email, other.email) && Objects.equals(fullname, other.fullname)
				&& gender == other.gender && id == other.id && Objects.equals(password, other.password)
				&& Objects.equals(phonenumber, other.phonenumber) && Objects.equals(role, other.role);
	}

}
