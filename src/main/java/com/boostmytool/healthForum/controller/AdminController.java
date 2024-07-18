package com.boostmytool.healthForum.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.boostmytool.healthForum.model.Account;
import com.boostmytool.healthForum.model.AccountDto;
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
@RequestMapping({"admin", "/admin"})
public class AdminController {
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
	@GetMapping("logout")
	public String logout(){
		account = null;
		profile = null;
		return "redirect:/login";
	}
	
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
	    
	    redirectAttributes.addAttribute("userName", account.getUserName());
	    
	    //create PostDto to add PostMain
	    PostDto postDto = new PostDto();
	    model.addAttribute("postDto", postDto);
		return "redirect:/admin";
	}
	
	@GetMapping({"", "/"})
	public String showPostMainPage(Model model, @RequestParam String userName,
				@RequestParam(defaultValue = "0") int page,
				@RequestParam(defaultValue = "3") int size){
		
		if(Arepo.findById(userName).isEmpty()) {
			return "redirect:/login";
		}
		
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "time"));
        Page<Post> postPage = Porepo.findAll(pageable);
		
        PostDto postDto = new PostDto();
        model.addAttribute("postDto", postDto);
        
	    model.addAttribute("posts", postPage.getContent());
	    model.addAttribute("currentPage", postPage.getNumber());
	    model.addAttribute("totalPages", postPage.getTotalPages());
	    model.addAttribute("totalItems", postPage.getTotalElements());
	    model.addAttribute("userName", account.getUserName());
		return "/admin/postMain";
	}
	
	@GetMapping("search")
	public String searchPosts(Model model, @RequestParam String title,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "3") int size,
			RedirectAttributes redirectAttributes) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "time"));
        Page<Post> postPage = Porepo.findByTitle(title, pageable);
        PostDto postDto = new PostDto();
        model.addAttribute("postDto", postDto);
        model.addAttribute("title", title);
        model.addAttribute("userName", account.getUserName());
	    model.addAttribute("posts", postPage.getContent());
	    model.addAttribute("currentPage", postPage.getNumber());
	    model.addAttribute("totalPages", postPage.getTotalPages());
	    model.addAttribute("totalItems", postPage.getTotalElements());
	    
		return "admin/searchPost";
	}
	
	@PostMapping("post")
	public String createPost(Model model,
			@Valid @ModelAttribute PostDto postDto,
			BindingResult result, RedirectAttributes redirectAttributes) {
		if(postDto.getImage() == null || postDto.getImage().isEmpty()) {
			result.addError(new FieldError("postDto", "image", "Image is required"));
		}
		
		if(result.hasErrors()) {
			return "redirect:/admin";
		}
		
		String upLoadDir = "public/post/";
		
		Post post = new Post();
		post.setUserNameProfile(profile.getUserNameProfile());
		post.setContent(postDto.getContent());
		post.setTitle(postDto.getTitle().toLowerCase());
		
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
		
		model.addAttribute("userName", account.getUserName());
		redirectAttributes.addAttribute("userName", account.getUserName());
		return "redirect:/admin";
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
		return "redirect:/admin";
	}
	
	@GetMapping("editpost")
	public String vieweditPost(Model model, @RequestParam long id) {
		Post post = Porepo.findById(id).get();
		PostDto postDto = new PostDto();
		
		System.out.println(post.getAvatar());
		
		postDto.setContent(post.getContent());
		postDto.setTime(post.getTime());
		postDto.setTitle(post.getTitle());
		postDto.setUserNameProfile(post.getUserNameProfile());
		
		model.addAttribute("avatar", post.getAvatar());
		model.addAttribute("image",post.getImage());
		model.addAttribute("postDto", postDto);
		model.addAttribute("id", id);
		model.addAttribute("userName", account.getUserName());
		return "admin/editPost";
	}
	
	@PostMapping("editpost")
	public String editPost(Model model, @RequestParam long id, 
			@Valid @ModelAttribute PostDto postDto,
			@Valid @ModelAttribute String avatar,
			RedirectAttributes redirectAttributes) {
		redirectAttributes.addAttribute("id", id);
		
		String upLoadDir = "public/post/";
		
		Post p = new Post();
	
		p.setId(id);
		p.setContent(postDto.getContent());
		if(postDto.getImage() != null) {
			MultipartFile postImage = postDto.getImage();
			try(InputStream inputStream = postImage.getInputStream()){
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
				Files.copy(inputStream, Paths.get(upLoadDir + p.getUserNameProfile()+ " " + LocalDateTime.now().format(formatter).toString() + ".png"), StandardCopyOption.REPLACE_EXISTING);
				p.setImage(p.getUserNameProfile() + " " + LocalDateTime.now().format(formatter).toString() + ".png");
			} 
			catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
		p.setTitle(postDto.getTitle());
		
		
		int updateResult = Porepo.updateByIdPost(id, p.getTitle(), p.getContent(), p.getImage());
		System.out.println("updateResult: " + updateResult);
		return "redirect:/admin/editpost";
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
		model.addAttribute("userName", account.getUserName());
		System.out.println(account.getUserName());
		return "/admin/postInformation";
	}
	
	@PostMapping("viewPost")
	public String viewPosts(Model model,@RequestParam Long id /*idPost*/, RedirectAttributes redirectAttributes,
			@Valid @ModelAttribute Comment cm,
			BindingResult result) {
		redirectAttributes.addAttribute("id",id);
		if(result.hasErrors()) {
			return "redirect:/admin/viewPost";
		}
		cm.setAvatar(profile.getAvatar());
		cm.setIdPost(id);
		cm.setUserNameProfile(profile.getUserNameProfile());
		Crepo.save(cm);
		return "redirect:/admin/viewPost";
	}
	
	@GetMapping({"profile"})
	public String showProfile(Model model, @RequestParam String userNameProfile) {
		if(!Prepo.findById(userNameProfile).isEmpty()) {
			ProfileDto profileDto = new ProfileDto();
			Profile p = Prepo.findById(userNameProfile).get();
			profileDto.setUserNameProfile(p.getUserNameProfile());
			profileDto.setName(p.getName());
			profileDto.setPhoneNumber(p.getPhoneNumber());
			profileDto.setSex(p.getSex());
			profileDto.setBirthDay(p.getBirthDay());
			
			model.addAttribute("userName", account.getUserName());
			model.addAttribute("profileDto", profileDto);
			model.addAttribute("avatarFile",p.getAvatar());
			return "/admin/profile";
		}
		return "redirect:/admin";
	}
	
	@PostMapping("profile")
	public String updateProfile(Model model,
			@Valid @ModelAttribute ProfileDto profileDto,
			BindingResult result, RedirectAttributes redirectAttributes) {
		
		//xử lý bug userNameProfile binding
		for(int i=0; i<profileDto.getUserNameProfile().length(); i++) {
			if(profileDto.getUserNameProfile().charAt(i) == ',') {
				profileDto.setUserNameProfile(profileDto.getUserNameProfile().substring(0, i));
			}
		}

		if(result.hasErrors()) {
			System.out.println(result.getAllErrors());
			return "/admin/profile";
		}
		
		Profile p = new Profile();
		p.setUserNameProfile(profileDto.getUserNameProfile());
		p.setAvatar(p.getUserNameProfile() + ".png");		
		String upLoadDir = "public/avatar/";
		String oleAvatar = profile.getAvatar();
		//save avatar
		MultipartFile newAvatar = profileDto.getAvatar();
		if(!newAvatar.isEmpty()) {
			try(InputStream inputStream = newAvatar.getInputStream()){
				Files.copy(inputStream, Paths.get( upLoadDir + p.getUserNameProfile() + ".png"), StandardCopyOption.REPLACE_EXISTING);
				p.setAvatar(p.getUserNameProfile() + ".png");
			} 
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		
		if(oleAvatar.equals("default.png") && !profile.getAvatar().equals("default.png")) {
			List<Post> posts = Porepo.findByUserNameProfile(profile.getUserNameProfile());
			for(Post po : posts) {
				po.setAvatar(profile.getAvatar());
			}
		}
		
		p.setBirthDay(profileDto.getBirthDay());
		p.setName(profileDto.getName());
		p.setPhoneNumber(profileDto.getPhoneNumber());
		p.setSex(profileDto.getSex());
		
		redirectAttributes.addAttribute("userNameProfile", p.getUserNameProfile());
		Prepo.save(p);
		if(p.getUserNameProfile().equals(profile.getUserNameProfile())) {
			profile = p;
		}
		return "redirect:/admin/profile";
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
		
		return "admin/postMain";
	}
	
	@GetMapping("profiles")
	public String viewProfiles(Model model,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "3") int size,
			RedirectAttributes redirectAttributes) {
		Pageable pageable = PageRequest.of(page, size);
		Page<Profile> profilePage = Prepo.findAll(pageable);
		
		model.addAttribute("userName", account.getUserName());
	    model.addAttribute("profiles", profilePage.getContent());
	    model.addAttribute("currentPage", profilePage.getNumber());
	    model.addAttribute("totalPages", profilePage.getTotalPages());
	    model.addAttribute("totalItems", profilePage.getTotalElements());
		return "/admin/profiles";
	}
	
	@GetMapping("profiles/search")
	public String searchProfiles(Model model,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "3") int size,
			@RequestParam String userNameProfile,
			RedirectAttributes redirectAttributes) {
		Pageable pageable = PageRequest.of(page, size);
		Page<Profile> profilePage = Prepo.findByUserNameProfile(userNameProfile, pageable);
		
		model.addAttribute("userName", account.getUserName());
	    model.addAttribute("profiles", profilePage.getContent());
	    model.addAttribute("currentPage", profilePage.getNumber());
	    model.addAttribute("totalPages", profilePage.getTotalPages());
	    model.addAttribute("totalItems", profilePage.getTotalElements());
		return "/admin/profiles";
	}
	
	@GetMapping("accounts")
	public String viewAccounts(Model model,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "6") int size,
			RedirectAttributes redirectAttributes) {
		Pageable pageable = PageRequest.of(page, size);
		Page<Account> accountPage = Arepo.findAll(pageable);
		
		model.addAttribute("userName", account.getUserName());
	    model.addAttribute("accounts", accountPage.getContent());
	    model.addAttribute("currentPage", accountPage.getNumber());
	    model.addAttribute("totalPages", accountPage.getTotalPages());
	    model.addAttribute("totalItems", accountPage.getTotalElements());
		return "/admin/accounts";
	}
	
	@GetMapping("accounts/search")
	public String searchAccounts(Model model,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "3") int size,
			@RequestParam String userNameAccount,
			RedirectAttributes redirectAttributes) {
		Pageable pageable = PageRequest.of(page, size);
		Page<Account> accountPage = Arepo.findByUserName(userNameAccount, pageable);
		
		model.addAttribute("userName", account.getUserName());
	    model.addAttribute("accounts", accountPage.getContent());
	    model.addAttribute("currentPage", accountPage.getNumber());
	    model.addAttribute("totalPages", accountPage.getTotalPages());
	    model.addAttribute("totalItems", accountPage.getTotalElements());
		return "/admin/accounts";
	}
	
	@GetMapping("accounts/deleteaccount")
	public String deleteAccount(@RequestParam String userName) {
		Account a = Arepo.findById(userName).get();
		
		Profile p = Prepo.findById(a.getUserNameProfile()).get();
		
		List<Comment> cm = p.getComment();
		for(Comment comment : cm) {
			Crepo.delete(comment);
		}
		
		List<Post> po = p.getPosts();
		for(Post post : po) {
			Crepo.deleteByFieldIdPost(post.getId());
			Porepo.deleteById(post.getId());
		}
		
		Arepo.delete(a);
		if(!p.getAvatar().equals("default.png")) {
			File fileToDelete = new File("public/avatar", p.getAvatar());
			 if (fileToDelete.exists()) {
		            // Thực hiện xóa file
		            if (fileToDelete.delete()) {
		                System.out.println("File deleted successfully.");
		            } else {
		                System.out.println("Failed to delete the file.");
		            }
		        } else {
		            System.out.println("File does not exist.");
		        }
		}
		Prepo.delete(p);
		return "redirect:/admin/accounts";
	}
	
	@GetMapping("accounts/editaccount")
	public String editAccount(Model model, @RequestParam String userName) {
		
		AccountDto accountDto = new AccountDto();
		Account a = Arepo.findById(userName).get();
		accountDto.setUserName(a.getUserName());
		accountDto.setPassWord(a.getPassWord());
		
		model.addAttribute("accountDto", accountDto);
		model.addAttribute("userName", accountDto.getUserName());
		return "/admin/editAccount";
	}
	
	@PostMapping("accounts/editaccount")
	public String viewEditAccount(@Valid @ModelAttribute AccountDto accountDto) {
		String editAccountUserName = "";
		
		for(int i=0; i<accountDto.getUserName().length(); i++) {
			if(accountDto.getUserName().charAt(i) == ',') {
				editAccountUserName = accountDto.getUserName().substring(0, i);
			}
		}
		
		Account a = Arepo.findById(editAccountUserName).get();
		a.setPassWord(accountDto.getPassWord());	
		Arepo.save(a);
		return "redirect:/admin/accounts";
	}
}
