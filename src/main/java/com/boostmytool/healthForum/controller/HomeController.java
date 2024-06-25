package com.boostmytool.healthForum.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.boostmytool.healthForum.model.Account;
import com.boostmytool.healthForum.model.Post;
import com.boostmytool.healthForum.model.PostDto;
import com.boostmytool.healthForum.model.Profile;
import com.boostmytool.healthForum.model.ProfileDto;
import com.boostmytool.healthForum.service.AccountRepository;
import com.boostmytool.healthForum.service.PostRepository;
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
	@Autowired
	private PostRepository Porepo;
	@GetMapping({"start"})
	public String showPostMainPage(@RequestParam String userName, RedirectAttributes redirectAttributes, Model model) {
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
	    
	    //create PostDto to add PostMain
	    PostDto postDto = new PostDto();
	    model.addAttribute("postDto", postDto);
		return "redirect:/home";
	}
	@GetMapping({"", "/"})
	public String showPostMainPage(Model model) {
		PostDto postDto = new PostDto();
	    model.addAttribute("postDto", postDto);
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
	
	@PostMapping("post")
	public String createPost(Model model,
			@Valid @ModelAttribute PostDto postDto,
			BindingResult result) {
		if(postDto.getImage() == null || postDto.getImage().isEmpty()) {
			result.addError(new FieldError("postDto", "image", "Image is required"));
		}
		
		if(result.hasErrors()) {
			return "redirect:/home";
		}
		
		String upLoadDir = "public/post/";
		
		Post post = new Post();
		post.setUserNameProfile(profile.getUserNameProfile());
		post.setContent(postDto.getContent());
		post.setTitle(postDto.getTitle());
		
		if(postDto.getImage() != null) {
			MultipartFile postImage = postDto.getImage();
			try(InputStream inputStream = postImage.getInputStream()){
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
				Files.copy(inputStream, Paths.get(upLoadDir + post.getUserNameProfile()+ " " + post.getTitle() + LocalDateTime.now().format(formatter).toString() + ".png"), StandardCopyOption.REPLACE_EXISTING);
				post.setImage(post.getUserNameProfile() + " " + post.getTitle() + LocalDateTime.now().format(formatter).toString() + ".png");
			} 
			catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}

		Porepo.save(post);
		
		return "redirect:/home";
	}
}
