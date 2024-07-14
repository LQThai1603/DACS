package com.boostmytool.healthForum.model;

import java.time.LocalDate;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotEmpty;

public class ProfileDtOAndroid {
	@NotEmpty(message = "UserNameProfile is required")
	private String userNameProfile;
	
	private String name;
	@NotEmpty(message = "PhoneNumber is required")
	private String phoneNumber;
	
	private LocalDate birthDay;
	
	private String sex;
	
	private byte[] avatar;
	
	public ProfileDtOAndroid() {}
	
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public byte[] getAvatar() {
		return avatar;
	}

	public void setAvatar(byte[] avatar) {
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
