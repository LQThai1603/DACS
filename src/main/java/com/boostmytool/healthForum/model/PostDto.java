package com.boostmytool.healthForum.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class PostDto {
	private long id;
	
	@NotEmpty(message = "Title of Post is required")
	private String title;
	
	@NotEmpty(message = "content of Post is required")
	private String content;
	
	@NotNull(message = "Image of Post is required")
	private MultipartFile image;
	
	private LocalDateTime time;
	
	private String avatar;
	
	private String userNameProfile;
	
	private List<Comment> comment;
	
	public PostDto() {
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public MultipartFile getImage() {
		return image;
	}

	public void setImage(MultipartFile image) {
		this.image = image;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LocalDateTime getTime() {
		return time;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getUserNameProfile() {
		return userNameProfile;
	}

	public void setUserNameProfile(String userNameProfile) {
		this.userNameProfile = userNameProfile;
	}

	public List<Comment> getComment() {
		return comment;
	}

	public void setComment(List<Comment> comment) {
		this.comment = comment;
	}
	
	
}
