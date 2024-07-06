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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import com.boostmytool.healthForum.model.Comment;
import com.boostmytool.healthForum.model.Post;
import com.boostmytool.healthForum.model.PostDto;
import com.boostmytool.healthForum.model.Profile;
import com.boostmytool.healthForum.model.ProfileDto;
import com.boostmytool.healthForum.service.AccountRepository;
import com.boostmytool.healthForum.service.CommentRepository;
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
	@Autowired
	private CommentRepository Crepo;
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
	public String showPostMainPage(Model model,
				@RequestParam(defaultValue = "0") int page,
				@RequestParam(defaultValue = "3") int size){
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "time"));
        Page<Post> postPage = Porepo.findAll(pageable);
		
        PostDto postDto = new PostDto();
        model.addAttribute("postDto", postDto);
        
	    model.addAttribute("posts", postPage.getContent());
	    model.addAttribute("currentPage", postPage.getNumber());
	    model.addAttribute("totalPages", postPage.getTotalPages());
	    model.addAttribute("totalItems", postPage.getTotalElements());
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
		if(!newAvatar.isEmpty()) {
			try(InputStream inputStream = newAvatar.getInputStream()){
				Files.copy(inputStream, Paths.get( upLoadDir + profile.getUserNameProfile() + ".png"), StandardCopyOption.REPLACE_EXISTING);
				profile.setAvatar(profile.getUserNameProfile() + ".png");
			} 
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
	
	@GetMapping("viewProfile")
	public String viewProfile(Model model, @RequestParam String userNameProfile) {
		if(profile.getUserNameProfile().equals(userNameProfile)) {
			return "redirect:/home/profile";
		}
		if(!Prepo.findById(userNameProfile).isEmpty()) {
			Profile pro = Prepo.findById(userNameProfile).get();

			model.addAttribute("profile", pro);
			return "/home/profileInformation";

		}		
		return "redirect:/home";
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
				Files.copy(inputStream, Paths.get(upLoadDir + post.getUserNameProfile()+ " " + LocalDateTime.now().format(formatter).toString() + ".png"), StandardCopyOption.REPLACE_EXISTING);
				post.setImage(post.getUserNameProfile() + " " + LocalDateTime.now().format(formatter).toString() + ".png");
			} 
			catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
		post.setAvatar(profile.getAvatar());
		
		Porepo.save(post);
		
		return "redirect:/home";
	}
	
	@GetMapping("viewPost")
	public String viewPost(Model model,@RequestParam Long id,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "3") int size) {

		Post post = Porepo.findById(id).get();
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "time"));
        Page<Comment> commentPage = Crepo.findByFieldIdPost(id, pageable);
		Comment comment = new Comment();
        
        model.addAttribute("currentPage", commentPage.getNumber());
	    model.addAttribute("totalPages", commentPage.getTotalPages());
	    model.addAttribute("totalItems", commentPage.getTotalElements());
        model.addAttribute("comments", commentPage.getContent());
		model.addAttribute("post", post);
		model.addAttribute("cm", comment);
		return "/home/postInformation";
	}
	
	@PostMapping("viewPost")
	public String viewPosts(Model model,@RequestParam Long id /*idPost*/, RedirectAttributes redirectAttributes,
			@Valid @ModelAttribute Comment cm,
			BindingResult result) {
		redirectAttributes.addAttribute("id",id);
		if(result.hasErrors()) {
			return "redirect:/home/viewPost";
		}
		cm.setAvatar(profile.getAvatar());
		cm.setIdPost(id);
		cm.setUserNameProfile(profile.getUserNameProfile());
		Crepo.save(cm);
		return "redirect:/home/viewPost";
	}
	
	@GetMapping("mypostnav")
	public String navigateMypost(RedirectAttributes redirectAttributes) {
		redirectAttributes.addAttribute("userName", profile.getUserNameProfile());
		return "redirect:/home/mypost";
	}
	
	@GetMapping("mypost")
	public String viewMyPosts(Model model, @RequestParam String userName,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "3") int size,
			RedirectAttributes redirectAttributes) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "time"));
        Page<Post> postPage = Porepo.findByUserNameProfile(userName, pageable);
        PostDto postDto = new PostDto();
        model.addAttribute("postDto", postDto);
        model.addAttribute("userName", userName);
	    model.addAttribute("posts", postPage.getContent());
	    model.addAttribute("currentPage", postPage.getNumber());
	    model.addAttribute("totalPages", postPage.getTotalPages());
	    model.addAttribute("totalItems", postPage.getTotalElements());
		
		return "home/myPost";
	}
	
	@GetMapping("deletepost")
	public String deletePost(@RequestParam long id, RedirectAttributes redirectAttributes) {
		redirectAttributes.addAttribute("userName", profile.getUserNameProfile());
		
		Post p = Porepo.findById(id).get();
		
		String upLoadDir = "public/post/";
		File file = new File(upLoadDir + p.getImage());
		if(file.exists()) {
			System.out.println("image of post is deleted!");
			file.delete();
		}
		//delete comment with this post
		Crepo.deleteByFieldIdPost(id);
		
		//delete post
		Porepo.deleteById(id);
		return "redirect:/home/mypost";
	}
}
