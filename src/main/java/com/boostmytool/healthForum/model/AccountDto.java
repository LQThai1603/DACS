package com.boostmytool.healthForum.model;

import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotEmpty;

public class AccountDto {
	@NotEmpty(message = "UserName is required")
	private String userName;
	@NotEmpty(message = "PassWord is required")
	private String passWord;
	
	public AccountDto(@NotEmpty(message = "UserName is required") String userName,
			@NotEmpty(message = "PassWord is required") String passWord) {
		super();
		this.userName = userName;
		this.passWord = passWord;
	}

	public AccountDto(){}
	
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
