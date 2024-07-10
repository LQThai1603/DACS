package com.boostmytool.healthForum.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern.List;

public class ForgotAccountDto {
	@NotEmpty(message = "UserName is required")
	private String userName;
	@NotEmpty(message = "PassWord is required")
	@Size(min = 10, message = "Password must be at least 10 characters")
    @Pattern.List({
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&+=])(?!.*\\s).*$", 
                 message = "Password must have at least one uppercase letter, one lowercase letter, one digit, one special character, and no whitespace")
    })
	private String passWord;
	@NotEmpty(message = "PassWord is required")
	private String conFirmPassWord;
	
	@NotEmpty(message = "PhoneNumber is required")
	private String phoneNumber;
	
	

	public ForgotAccountDto(@NotEmpty(message = "UserName is required") String userName,
			@NotEmpty(message = "PassWord is required") @Size(min = 10, message = "Password must be at least 10 characters") @List(@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&+=])(?!.*\\s).*$", message = "Password must have at least one uppercase letter, one lowercase letter, one digit, one special character, and no whitespace")) String passWord,
			@NotEmpty(message = "PassWord is required") String conFirmPassWord,
			@NotEmpty(message = "PhoneNumber is required") String phoneNumber) {
		super();
		this.userName = userName;
		this.passWord = passWord;
		this.conFirmPassWord = conFirmPassWord;
		this.phoneNumber = phoneNumber;
	}

	public ForgotAccountDto(){}
	
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

	public String getConFirmPassWord() {
		return conFirmPassWord;
	}

	public void setConFirmPassWord(String conFirmPassWord) {
		this.conFirmPassWord = conFirmPassWord;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}
