package com.boostmytool.healthForum.model;

import java.time.LocalDate;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;

public class ProfileDto {
	@NotEmpty(message = "UserNameProfile is required")
	private String userNameProfile;
	
	private String name;
	@NotEmpty(message = "PhoneNumber is required")
	private String phoneNumber;
	
	private LocalDate birthDay;
	
	private String sex;
	
	private MultipartFile avatar;

	public ProfileDto(@NotEmpty(message = "PassWord is required") String userNameProfile, 
			String name,
			@NotEmpty(message = "PhoneNumber is required") String phoneNumber, 
			LocalDate birthDay,
			String sex,
			MultipartFile avatar) {
		super();
		this.userNameProfile = userNameProfile;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.birthDay = birthDay;
		this.sex = sex;
		this.avatar = avatar;
	}
	
	public ProfileDto() {}
	
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public MultipartFile getAvatar() {
		return avatar;
	}

	public void setAvatar(MultipartFile avatar) {
		this.avatar = avatar;
	}

	public String getUserNameProfile() {
		return userNameProfile;
	}

	public void setUserNameProfile(String userNameProfile) {
		this.userNameProfile = userNameProfile;
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

	public LocalDate getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(LocalDate birthDay) {
		this.birthDay = birthDay;
	}
	
	
}
