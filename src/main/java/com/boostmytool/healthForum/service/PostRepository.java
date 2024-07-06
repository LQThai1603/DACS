package com.boostmytool.healthForum.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.boostmytool.healthForum.model.Post;

public interface PostRepository extends JpaRepository<Post, Long>{
	 Page<Post> findAll(Pageable pageable);
	 
	 @Query("SELECT p FROM Post p WHERE p.userNameProfile = :userName")
	 Page<Post> findByUserNameProfile(String userName, Pageable pageable);
}
