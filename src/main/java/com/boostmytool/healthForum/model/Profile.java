package com.boostmytool.healthForum.model;

import java.time.LocalDate;

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
	private LocalDate birthDay;
	
	@Column(name = "sex")
	private String sex;
	
	@Column(name = "avatar")
	private String avatar;
	
	@OneToOne(mappedBy = "profile")
    private Account account;
	
	public Profile(@NotEmpty(message = "PassWord is required") String userNameProfile, 
			String name, 
			String phoneNumber,
			LocalDate birthDay,
			String sex,
			String avatar) {
		super();
		this.userNameProfile = userNameProfile;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.birthDay = birthDay;
		this.sex = sex;
		this.avatar = avatar;
	}
	
	public Profile() {
		this.userNameProfile = "NouserNameProfile";
		this.name = "NoName";
		this.phoneNumber = "NoPhone";
		this.birthDay = LocalDate.now();
		this.sex = "Other";
		this.avatar = "NoAvartar";
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

	public String getUserNameProfile() {
		return userNameProfile;
	}

	public void setUserNameProfile(String userNameProfile) {
		this.userNameProfile = userNameProfile;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}
	
}
