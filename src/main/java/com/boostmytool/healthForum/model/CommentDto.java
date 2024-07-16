package com.boostmytool.healthForum.model;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class CommentDto {
    private long id;

    private String userNameProfile;

    private long idPost;

    private LocalDateTime time;
    
    @NotEmpty(message = "content of Post is required")
    private String content;

    private String avatar;

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
