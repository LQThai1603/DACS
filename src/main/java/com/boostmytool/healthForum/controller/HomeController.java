package com.boostmytool.healthForum.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.boostmytool.healthForum.model.Account;
import com.boostmytool.healthForum.model.Profile;
import com.boostmytool.healthForum.model.ProfileDto;
import com.boostmytool.healthForum.service.AccountRepository;
import com.boostmytool.healthForum.service.ProfileRepository;

import jakarta.validation.Valid;

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
	public String showPostMainPage(@RequestParam String userName, RedirectAttributes redirectAttributes) {
		Optional<Account> accountOpt = Arepo.findById(userName);
	    if (!accountOpt.isPresent()) {
	        redirectAttributes.addFlashAttribute("error", "Tài khoản không tồn tại....");
	        return "redirect:/login";
	    }
	    account = accountOpt.get();

	    Optional<Profile> profileOpt = Prepo.findById(userName);
	    if (!profileOpt.isPresent()) {
	        redirectAttributes.addFlashAttribute("error", "Hồ sơ không tồn tại.");
	        return "redirect:/login";
	    }
	    profile = profileOpt.get();
		return "redirect:/home";
	}
	@GetMapping({"", "/"})
	public String showPostMainPage() {
		return "/home/postMain";
	}
	@GetMapping({"profile"})
	public String showProfile(Model model) {
		ProfileDto profileDto = new ProfileDto();
		profileDto.setUserNameProfile(profile.getUserNameProfile());
		profileDto.setName(profile.getName());
		profileDto.setPhoneNumber(profile.getPhoneNumber());
		profileDto.setSex(profile.getSex());
		profileDto.setBirthDay(profile.getBirthDay());

		model.addAttribute("profileDto", profileDto);
		model.addAttribute("avatarFile",profile.getAvatar());
		return "/home/profile";
	}
	
	@PostMapping("profile")
	public String updateProfile(Model model,
			@Valid @ModelAttribute ProfileDto profileDto,
			BindingResult result) {
			
		if(result.hasErrors()) {
			System.out.println(result.getAllErrors());
			return "/home/profile";
		}
		
		String upLoadDir = "public/avatar/";
		
		//save avatar
		MultipartFile newAvatar = profileDto.getAvatar();
		try(InputStream inputStream = newAvatar.getInputStream()){
			Files.copy(inputStream, Paths.get( upLoadDir + profile.getUserNameProfile() + ".png"), StandardCopyOption.REPLACE_EXISTING);
			profile.setAvatar(profile.getUserNameProfile() + ".png");
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		profile.setBirthDay(profileDto.getBirthDay());
		profile.setName(profileDto.getName());
		profile.setPhoneNumber(profileDto.getPhoneNumber());
		profile.setSex(profileDto.getSex());
		
		
		model.addAttribute("profile", profile);
		model.addAttribute("avatarFile", profile.getAvatar());
		Prepo.save(profile);
		return "/home/profile";
	}
}
