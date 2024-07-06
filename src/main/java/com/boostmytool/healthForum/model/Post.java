package com.boostmytool.healthForum.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "post")
public class Post {
	@Id
	@Column(name = "id")
	@NotNull
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "title", columnDefinition = "TEXT")
	private String title;
	
	@Column(name = "content", columnDefinition = "TEXT")
	private String content;
	
	@Column(name = "image")
	private String image;
	
	@Column(name = "time")
	private LocalDateTime time;
	
	@Column(name = "avatar")
	private String avatar;
	
	@ManyToOne
	@JoinColumn(name = "usernameprofile", referencedColumnName = "usernameprofile", insertable = false, updatable = false)
	private Profile profile;
	
	@NotEmpty(message = "UserName of Post is required")
	@Column(name = "usernameprofile")
	private String userNameProfile;

	@OneToMany(mappedBy = "post")
	private List<Comment> comment;
	
	public Post(@NotEmpty(message = "ID of Post is required") long id, String title, String content, String image,
			String userNameProfile, LocalDateTime time, String avatar) {
		super();
		this.id = id;
		this.title = title;
		this.content = content;
		this.image = image;
		this.userNameProfile = userNameProfile;
		this.time = time;
		this.avatar = avatar;
	}
	
	public Post() {
		
		this.image = "default.png";
		this.time = LocalDateTime.now();
	}

	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	public String getUserNameProfile() {
		return userNameProfile;
	}

	public void setUserNameProfile(String userNameProfile) {
		this.userNameProfile = userNameProfile;
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
	
	
}
