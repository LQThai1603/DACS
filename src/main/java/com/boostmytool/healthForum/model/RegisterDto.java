package com.boostmytool.healthForum.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Pattern.List;
import jakarta.validation.constraints.Size;

public class RegisterDto {
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
	
	
	
	
	public RegisterDto(@NotEmpty(message = "UserName is required") String userName,
			@NotEmpty(message = "PassWord is required") @Size(min = 10, message = "Password must be at least 10 characters") @List(@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=])(?!.*\\s).*$", message = "Password must have at least one uppercase letter, one lowercase letter, one digit, one special character, and no whitespace")) String passWord,
			@NotEmpty(message = "PassWord is required") String conFirmPassWord) {
		super();
		this.userName = userName;
		this.passWord = passWord;
		this.conFirmPassWord = conFirmPassWord;
	}

	public RegisterDto(){}
	
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
	
}
