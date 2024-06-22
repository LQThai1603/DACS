package com.boostmytool.healthForum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.boostmytool.healthForum.model.Account;
import com.boostmytool.healthForum.model.Profile;
import com.boostmytool.healthForum.model.ProfileDto;
import com.boostmytool.healthForum.service.AccountRepository;
import com.boostmytool.healthForum.service.ProfileRepository;

@Controller
@RequestMapping({"home", "/home"})
public class HomeController {
	private Account account = null;
	private Profile profile = null;
	@Autowired
	private AccountRepository Arepo;
	@Autowired
	private ProfileRepository Prepo;
	@GetMapping({"start"})
	public String showPostMainPage(@RequestParam String userName) {
		account = Arepo.findById(userName).get();
		profile = Prepo.findById(userName).get();
		return "home/postMain";
	}
	@GetMapping({"", "/"})
	public String showPostMainPage() {
		return "home/postMain";
	}
	@GetMapping({"profile"})
	public String showProfile(Model model) {
		ProfileDto profileDto = new ProfileDto();
		profileDto.setUserNameProfile(profile.getUserNameProfile());
		profileDto.setName(profile.getName());
		profileDto.setPhoneNumber(profile.getPhoneNumber());
		profileDto.setSex(profile.getSex());
		profileDto.setBirthDay(profile.getBirthDay());
		profileDto.setAvatar(profile.getAvatar());
		System.out.println(profileDto.getUserNameProfile());
		model.addAttribute("profileDto", profileDto);
		return "home/profile";
	}
}
