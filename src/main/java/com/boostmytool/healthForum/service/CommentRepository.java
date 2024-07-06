package com.boostmytool.healthForum.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.boostmytool.healthForum.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>{
	Page<Comment> findAll(Pageable pageable);
	
	@Query("SELECT c FROM Comment c WHERE c.idPost = :idPost")
    Page<Comment> findByFieldIdPost(long idPost, Pageable pageable);
	
	@Modifying
	@Transactional
	@Query("DELETE FROM Comment c WHERE c.idPost = :idPost")
	int deleteByFieldIdPost(long idPost);
}
