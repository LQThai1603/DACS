package com.boostmytool.healthForum.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.StackWalker.Option;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.boostmytool.healthForum.model.Account;
import com.boostmytool.healthForum.model.AccountDto;
import com.boostmytool.healthForum.model.Comment;
import com.boostmytool.healthForum.model.CommentDto;
import com.boostmytool.healthForum.model.Post;
import com.boostmytool.healthForum.model.PostDto;
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
	
	public byte[] convertImageToByteArray(String imagePath) {
        File file = new File(imagePath);
        byte[] imageBytes = null;
        
        try (FileInputStream fis = new FileInputStream(file);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            imageBytes = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return imageBytes;
    }
	
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
	
	@GetMapping("show/profile/{userNameProfile}")
    public ResponseEntity<Profile> getProfileById(@PathVariable String userNameProfile) {
        Optional<Profile> profile = Prepo.findById(userNameProfile);
        return profile.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
	
	@GetMapping("show/posts")
	public ResponseEntity<List<Post>> getAllPost(){
		List<Post> posts = Porepo.findAll();
		return new ResponseEntity<List<Post>>(posts, HttpStatus.OK);
	}

	@GetMapping("/show/posts/user/{userNameProfile}")
    public ResponseEntity<List<Post>> getFilteredPosts(@PathVariable String userNameProfile) {
        List<Post> allPosts = Porepo.findAll();
        List<Post> filteredPosts = allPosts.stream()
                .filter(post -> post.getUserNameProfile().equals(userNameProfile)) // Lọc theo userNameProfile hoặc bất kỳ thuộc tính nào
                .collect(Collectors.toList());

        if (filteredPosts.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(filteredPosts, HttpStatus.OK);
    }

	@GetMapping("/show/posts/title/{title}")
    public ResponseEntity<List<Post>> getTitlePosts(@PathVariable String title) {
        List<Post> allPosts = Porepo.findAll();
        List<Post> filteredPosts = allPosts.stream()
                .filter(post -> post.getTitle().equals(title)) // Lọc theo userNameProfile hoặc bất kỳ thuộc tính nào
                .collect(Collectors.toList());

        if (filteredPosts.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(filteredPosts, HttpStatus.OK);
    }

	@GetMapping("/show/posts/content/{content}")
    public ResponseEntity<List<Post>> getContentPosts(@PathVariable String content) {
        List<Post> allPosts = Porepo.findAll();
        List<Post> filteredPosts = allPosts.stream()
                .filter(post -> post.getContent().equals(content)) // Lọc theo userNameProfile hoặc bất kỳ thuộc tính nào
                .collect(Collectors.toList());

        if (filteredPosts.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(filteredPosts, HttpStatus.OK);
    }
	
	@GetMapping("show/post/{idPost}")
	public ResponseEntity<Post> getPostById(@PathVariable long idPost){
		Optional<Post> post = Porepo.findById(idPost);
		return post.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}


	@GetMapping("show/comments/{idPost}") //lấy tất cả comment đã tồn tại trong bài post có id là idPost
	public ResponseEntity<List<Comment>> getComments(@PathVariable long idPost){
		List<Comment> c = Crepo.findByFieldIdPost(idPost);
		return ResponseEntity.ok(c);
	}
	
	
	@GetMapping("show/postImage{FileImage}")
	public ResponseEntity<byte[]> getPostImage(@PathVariable String FileImage){
		byte[] byteImage = convertImageToByteArray("public/post/" + FileImage);
		
		return ResponseEntity.ok(byteImage);
	}
	
	@GetMapping("show/avatar{FileImage}")
	public ResponseEntity<byte[]> getAvatar(@PathVariable String FileImage){
		byte[] byteImage = convertImageToByteArray("public/avatar/" + FileImage);
		return ResponseEntity.ok(byteImage);
	}
	
	@PostMapping("create/account")
	public ResponseEntity<Account> createAccount(
			@Valid @RequestBody AccountDto accountDto /*//chuyển đổi JSON được gửi bởi client thành đối tượng AccountDto*/){
		
		Account account = new Account();
		account.setUserName(accountDto.getUserName().trim());
		account.setPassWord(accountDto.getPassWord().trim());
		Account saveAccount = Arepo.save(account);
		Profile profile = new Profile();
		profile.setUserNameProfile(saveAccount.getUserName());
		Prepo.save(profile);
		return ResponseEntity.status(HttpStatus.CREATED).body(saveAccount);
	}
	
	@PostMapping(value = "/create/post", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> createPost(
			@Valid @ModelAttribute PostDto postDto,
			BindingResult result) {

		if (result.hasErrors()) {
			return ResponseEntity.badRequest().body(result.getAllErrors());
		}

		String uploadDir = "public/post/";

		Post post = new Post();
		post.setUserNameProfile(postDto.getUserNameProfile());
		post.setContent(postDto.getContent());
		post.setTitle(postDto.getTitle().toLowerCase());

		try {
			MultipartFile image = postDto.getImage();
			if (image != null && !image.isEmpty()) {
				// Xử lý lưu file ảnh
				String fileName = generateFileName(image);
				Files.copy(image.getInputStream(), Paths.get(uploadDir + fileName), StandardCopyOption.REPLACE_EXISTING);
				post.setImage(fileName);
			}

			// Đặt thời gian và avatar từ postDto
			post.setTime(LocalDateTime.now());
			post.setAvatar(postDto.getAvatar());

			// Lưu post vào cơ sở dữ liệu
			Post createdPost = Porepo.save(post);

			return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
		} catch (Exception e) {
			System.out.println("Error creating post: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}



	private String generateFileName(MultipartFile multiPart) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		return multiPart.getOriginalFilename().replace(" ", "_").toLowerCase() + "_" + LocalDateTime.now().format(formatter);
	}




    @PostMapping("/create/comment/{idPost}")
	public ResponseEntity<Comment> createComment(@Valid @RequestBody CommentDto commentModel) {
		Comment comment = new Comment();
		comment.setAvatar(commentModel.getAvatar());
		comment.setContent(commentModel.getContent());
		comment.setIdPost(commentModel.getIdPost());
		comment.setTime(LocalDateTime.now());
		comment.setUserNameProfile(commentModel.getUserNameProfile());

		Comment createdComment = Crepo.save(comment);

		return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
	}


	@PostMapping("edit/account")
	public ResponseEntity<Account> updateAccount(@Valid @RequestBody AccountDto accountDto){
		String editAccountUserName = "";
		
		for(int i=0; i<accountDto.getUserName().length(); i++) {
			if(accountDto.getUserName().charAt(i) == ',') {
				editAccountUserName = accountDto.getUserName().substring(0, i);
			}
		}
		
		Account a = Arepo.findById(editAccountUserName).get();
		a.setPassWord(accountDto.getPassWord());	
		Account accountEdit = Arepo.save(a);
		
		return ResponseEntity.ok(accountEdit);
	}
	
	@PutMapping("edit/profile/{userNameProfile}")
	public ResponseEntity<Profile> updateProfile(@PathVariable String userNameProfile,
												@Valid @ModelAttribute ProfileDto profileDto) {
		Optional<Profile> profileOpt = Prepo.findById(userNameProfile);
		if (!profileOpt.isPresent()) {
			return ResponseEntity.notFound().build();
		}

		Profile profile = profileOpt.get();
		String upLoadDir = "public/avatar/";

		// Save avatar
		MultipartFile newAvatar = profileDto.getAvatar();
		if (newAvatar != null && !newAvatar.isEmpty()) {
			try (InputStream inputStream = newAvatar.getInputStream()) {
				Files.copy(inputStream, Paths.get(upLoadDir + profile.getUserNameProfile() + ".png"), 
						StandardCopyOption.REPLACE_EXISTING);
				profile.setAvatar(profile.getUserNameProfile() + ".png");
			} catch (IOException e) {
				e.printStackTrace();
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
									.body(null);
			}
		}

		profile.setBirthDay(profileDto.getBirthDay());
		profile.setName(profileDto.getName().trim());
		profile.setPhoneNumber(profileDto.getPhoneNumber().trim());
		profile.setSex(profileDto.getSex().trim());

		Profile updatedProfile = Prepo.save(profile);
		return ResponseEntity.ok(updatedProfile);
	}

	
	@PutMapping("edit/post/{id}")
	public ResponseEntity<Post> updatePost(@PathVariable long id, 
			@Valid @ModelAttribute PostDto postDto,
			RedirectAttributes redirectAttributes){
		
		String upLoadDir = "public/post/";
		
		Post p = new Post();

		p.setId(id);
		p.setContent(postDto.getContent());
		if(postDto.getImage() != null) {
			MultipartFile postImage = postDto.getImage();
			try(InputStream inputStream = postImage.getInputStream()){
				String originalFileName = postImage.getOriginalFilename();
				String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
				String fileName = postDto.getUserNameProfile().toLowerCase() + " " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + extension;
				Files.copy(postImage.getInputStream(), Paths.get(upLoadDir + fileName), StandardCopyOption.REPLACE_EXISTING);
				p.setImage(fileName);
			} 
			catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
		p.setTitle(postDto.getTitle());

		Porepo.updateByIdPost(id, p.getTitle(), p.getContent(), p.getImage());
		Post updatePost = Porepo.findById(id).get();

		return ResponseEntity.ok(updatePost);
	}

	
	@PostMapping("delete/account/{userName}")
	public ResponseEntity<String> deleteAccount(@PathVariable String userName){
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
		Prepo.delete(p);
		return ResponseEntity.ok("Account and related data deleted successfully.");
	}
	
	@DeleteMapping("delete/post/{id}")
	public ResponseEntity<String> deletePost(@PathVariable long id){
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
		return ResponseEntity.ok("Post deleted successfully.");
	}
	
	
}