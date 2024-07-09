package com.boostmytool.healthForum.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.boostmytool.healthForum.model.Account;

public interface AccountRepository extends JpaRepository<Account, String>{
	 @Modifying
	 @Transactional
	 @Query("UPDATE Account a SET a.userName = :userName, a.passWord = :passWord " +
		       "WHERE a.userName = :userName " +
		       "AND a.userNameProfile IN " +
		       "(SELECT p.userNameProfile FROM Profile p WHERE p.userNameProfile = a.userNameProfile AND p.phoneNumber = :phoneNumber)")
	 int forgotPassWord(String userName, String passWord, String phoneNumber);
	 
}
