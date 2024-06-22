package com.boostmytool.healthForum.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "profile")
public class Profile {
	@Id
	@NotEmpty(message = "PassWord is required")
	@Column(name = "usernameprofile")
	private String userNameProfile;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "phonenumber")
	private String phoneNumber;
	
	@Column(name = "birthday")
	private LocalDateTime birthDay;
	
	@OneToOne(mappedBy = "profile")
    private Account account;
	
	public Profile(@NotEmpty(message = "PassWord is required") String userNameProfile, String name, String phoneNumber,
			LocalDateTime birthDay) {
		super();
		this.userNameProfile = userNameProfile;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.birthDay = birthDay;
	}
	
	public Profile() {}

	public String getuserNameProfile() {
		return userNameProfile;
	}

	public void setuserNameProfile(String userName) {
		this.userNameProfile = userName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public LocalDateTime getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(LocalDateTime birthDay) {
		this.birthDay = birthDay;
	}
}
