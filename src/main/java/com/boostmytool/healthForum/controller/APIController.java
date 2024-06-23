package com.boostmytool.healthForum.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boostmytool.healthForum.model.Account;
import com.boostmytool.healthForum.model.AccountDto;
import com.boostmytool.healthForum.model.Profile;
import com.boostmytool.healthForum.service.AccountRepository;
import com.boostmytool.healthForum.service.ProfileRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class APIController {
	@Autowired
	private AccountRepository Arepo;
	
	@Autowired
	private ProfileRepository Prepo;
	
	@GetMapping("show/accounts")
	public ResponseEntity<List<Account>> getAllAccount(){
		List<Account> accounts = Arepo.findAll();
		return new ResponseEntity<List<Account>>(accounts, HttpStatus.OK);
	}
	
	@GetMapping("show/account/{userName}")
    public ResponseEntity<Account> getProductById(@PathVariable String userName) {
		Optional<Account> account = Arepo.findById(userName);
        return account.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
	
	@GetMapping("show/profiles")
	public ResponseEntity<List<Profile>> getAllProfile(){
		List<Profile> profiles = Prepo.findAll();
		return new ResponseEntity<List<Profile>>(profiles, HttpStatus.OK);
	}
	
	@GetMapping("show/account/{userName}")
	public ResponseEntity<Profile> getProfile (@PathVariable String userNameProfile){
		Optional<Profile> profile = Prepo.findById(userNameProfile);
		return profile.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}
	
	@PostMapping("create/account")
	public ResponseEntity<Account> createAccount(
			@Valid @RequestBody AccountDto accountDto /*//chuyển đổi JSON được gửi bởi client thành đối tượng AccountDto*/){
		
		Account account = new Account();
		account.setUserName(accountDto.getUserName());
		account.setPassWord(accountDto.getPassWord());
		Account saveAccount = Arepo.save(account);
		return ResponseEntity.status(HttpStatus.CREATED).body(saveAccount);
	}
	
	
}
