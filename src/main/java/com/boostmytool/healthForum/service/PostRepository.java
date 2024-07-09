package com.boostmytool.healthForum.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.boostmytool.healthForum.model.Post;

public interface PostRepository extends JpaRepository<Post, Long>{
	 Page<Post> findAll(Pageable pageable);
	 
	 @Query("SELECT p FROM Post p WHERE p.userNameProfile = :userName")
	 Page<Post> findByUserNameProfile(String userName, Pageable pageable);
	 
	 @Query("SELECT p FROM Post p WHERE p.title LIKE %:title%")
	 Page<Post> findByTitle(String title, Pageable pageable);
	 
	 @Query("SELECT p FROM Post p WHERE p.userNameProfile = :userName AND p.title LIKE %:title%")
	 Page<Post> findByUserNameProfileAndTitle(String userName, String title, Pageable pageable);
	 
	 @Modifying
	 @Transactional
	 @Query("UPDATE Post p SET p.title = :title, p.content= :content, p.image = :image WHERE id = :id")
	 int updateByIdPost(long id, String title, String content, String image);
}
