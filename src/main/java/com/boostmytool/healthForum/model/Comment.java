package com.boostmytool.healthForum.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="Comment")
public class Comment {
	@Column(name="id")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@ManyToOne
	@JoinColumn(name = "usernameprofile", referencedColumnName = "usernameprofile", insertable = false, updatable = false)
	private Profile profile;
	
	@Column(name="usernameprofile")
	private String userNameProfile;
	
	@ManyToOne
	@JoinColumn(name = "idpost", referencedColumnName = "id", insertable = false, updatable = false)
	private Post post;
	
	@Column(name="idpost")
	private long idPost;
	
	@Column(name="time")
	private LocalDateTime time;
	
	@Column(name="content", columnDefinition = "TEXT")
	@NotEmpty(message = "Content is required")
	private String content;
	
	@Column(name="avatar")
	private String avatar;

	public Comment(long id, String userNameProfile, long idPost, LocalDateTime time, String content, String avatar) {
		super();
		this.id = id;
		this.userNameProfile = userNameProfile;
		this.idPost = idPost;
		this.time = time;
		this.content = content;
		this.avatar = avatar;
	}
	
	public Comment() {
		this.time = LocalDateTime.now();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserNameProfile() {
		return userNameProfile;
	}

	public void setUserNameProfile(String userNameProfile) {
		this.userNameProfile = userNameProfile;
	}

	public long getIdPost() {
		return idPost;
	}

	public void setIdPost(long idPost) {
		this.idPost = idPost;
	}

	public LocalDateTime getTime() {
		return time;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
	
}
