package com.boostmytool.healthForum.model;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotEmpty;

public class ProfileDto {
	@NotEmpty(message = "PassWord is required")
	private String userName;
	
	private String name;
	@NotEmpty(message = "PhoneNumber is required")
	private String phoneNumber;
	
	private LocalDateTime birthDay;

	public ProfileDto(@NotEmpty(message = "PassWord is required") String userName, String name,
			@NotEmpty(message = "PhoneNumber is required") String phoneNumber, LocalDateTime birthDay) {
		super();
		this.userName = userName;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.birthDay = birthDay;
	}
	
	public ProfileDto() {}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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
