package com.boostmytool.healthForum.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

@Entity
@Table(name = "account")
public class Account {	
	@Id
	@Column(name = "username")
	private String userName;
	
	@Column(name = "password")
	private String passWord;

	@OneToOne(cascade = CascadeType.ALL)
	@JsonBackReference
	@JoinColumn(name = "usernameprofile", referencedColumnName = "usernameprofile", insertable = false, updatable = false)
	private Profile profile;
	
	@Column(name = "usernameprofile")
	private String userNameProfile;
	
	public Account(String userName, String passWord, Profile profile, String userNameProfile) {
		super();
		this.userName = userName;
		this.passWord = passWord;
		this.userNameProfile = userNameProfile;
		this.profile = profile;
	}

	public Account(String userName, String passWord, String userNameProfile) {
		super();
		this.userName = userName;
		this.passWord = passWord;
		this.userNameProfile = userNameProfile;
	}
	
	public Account() {}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	
	
}
