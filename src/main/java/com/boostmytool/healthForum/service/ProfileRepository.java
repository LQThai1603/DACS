package com.boostmytool.healthForum.service;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.boostmytool.healthForum.model.Profile;

public interface ProfileRepository extends JpaRepository<Profile, String>{
	@Modifying
	@Transactional
	@Query("UPDATE Profile p SET p.name = :name, p.phoneNumber = :phoneNumber, p.birthDay = :birthDay, p.sex = :sex WHERE p.userNameProfile = :userNameProfile")
	int updateByUserNameProfile(String name, String phoneNumber, LocalDate birthDay, String sex, String userNameProfile);
}
