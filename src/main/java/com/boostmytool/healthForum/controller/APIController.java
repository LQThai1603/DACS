package com.boostmytool.healthForum.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.boostmytool.healthForum.model.Account;
import com.boostmytool.healthForum.model.AccountDto;
import com.boostmytool.healthForum.model.Profile;
import com.boostmytool.healthForum.model.ProfileDto;
import com.boostmytool.healthForum.service.AccountRepository;
import com.boostmytool.healthForum.service.CommentRepository;
import com.boostmytool.healthForum.service.PostRepository;
import com.boostmytool.healthForum.service.ProfileRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class APIController {
	@Autowired
	private AccountRepository Arepo;

	@Autowired
	private ProfileRepository Prepo;

	@Autowired
	private PostRepository Porepo;

	@Autowired
	private CommentRepository Crepo;

	@GetMapping("show/accounts")
	public ResponseEntity<List<Account>> getAllAccount() {
		List<Account> accounts = Arepo.findAll();
		return new ResponseEntity<List<Account>>(accounts, HttpStatus.OK);
	}

	@GetMapping("show/account/{userName}")
	public ResponseEntity<Account> getProductById(@PathVariable String userName) {
		Optional<Account> account = Arepo.findById(userName);
		return account.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping("show/profiles")
	public ResponseEntity<List<Profile>> getAllProfile() {
		List<Profile> profiles = Prepo.findAll();
		return new ResponseEntity<List<Profile>>(profiles, HttpStatus.OK);
	}

	@GetMapping("show/profile/{userNameProfile}")
	public ResponseEntity<Profile> getProfileById(@PathVariable String userNameProfile) {
		Optional<Profile> profile = Prepo.findById(userNameProfile);
		return profile.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping("create/account")
	public ResponseEntity<Account> createAccount(
			@Valid @RequestBody AccountDto accountDto /*
														 * //chuyển đổi JSON được gửi bởi client thành đối tượng
														 * AccountDto
														 */) {

		Account account = new Account();
		account.setUserName(accountDto.getUserName().trim());
		account.setPassWord(accountDto.getPassWord().trim());
		Account saveAccount = Arepo.save(account);
		Profile profile = new Profile();
		profile.setUserNameProfile(saveAccount.getUserName());
		Prepo.save(profile);
		return ResponseEntity.status(HttpStatus.CREATED).body(saveAccount);
	}

	@PostMapping("edit/profile/{userNameProfile}")
	public ResponseEntity<Profile> updateProfile(
			@PathVariable String userNameProfile,
			@RequestParam("name") String name,
			@RequestParam("phoneNumber") String phoneNumber,
			@RequestParam("birthDay") String birthDay,
			@RequestParam("sex") String sex,
			@RequestPart(value = "avatar", required = false) MultipartFile avatar) {

		Optional<Profile> profileOpt = Prepo.findById(userNameProfile);
		if (!profileOpt.isPresent()) {
			return ResponseEntity.notFound().build();
		}

		Profile profile = profileOpt.get();
		String upLoadDir = "public/avatar/";

		// save avatar
		if (avatar != null) {
			try (InputStream inputStream = avatar.getInputStream()) {
				Files.copy(inputStream, Paths.get(upLoadDir + profile.getUserNameProfile() + ".png"),
						StandardCopyOption.REPLACE_EXISTING);
				profile.setAvatar(profile.getUserNameProfile() + ".png");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		profile.setBirthDay(LocalDate.parse(birthDay)); // Chuyển đổi từ String sang LocalDate
		profile.setName(name.trim());
		profile.setPhoneNumber(phoneNumber.trim());
		profile.setSex(sex.trim());

		Profile updatedProfile = Prepo.save(profile);
		return ResponseEntity.ok(updatedProfile);
	}
}
