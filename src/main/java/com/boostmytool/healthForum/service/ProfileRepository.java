package com.boostmytool.healthForum.service;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boostmytool.healthForum.model.Profile;

public interface ProfileRepository extends JpaRepository<Profile, String>{
	
}
