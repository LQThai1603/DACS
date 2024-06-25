package com.boostmytool.healthForum.model;

import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotEmpty;

public class PostDto {

	
	@NotEmpty(message = "Title of Post is required")
	private String title;
	
	@NotEmpty(message = "content of Post is required")
	private String content;
	
	private MultipartFile image;

	public PostDto(
			@NotEmpty(message = "Title of Post is required") String title,
			@NotEmpty(message = "content of Post is required") String content,
			MultipartFile image
			) {
		super();
		this.title = title;
		this.content = content;
		this.image = image;
	}
	
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
}
