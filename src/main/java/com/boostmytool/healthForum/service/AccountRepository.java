package com.boostmytool.healthForum.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.boostmytool.healthForum.model.Account;

public interface AccountRepository extends JpaRepository<Account, String>{
	Page<Account> findAll(Pageable pageable);
	
	@Query("SELECT a FROM Account a WHERE a.userName LIKE %:userName%")
	Page<Account> findByUserName(String userName, Pageable pageable);
	
	@Modifying
	@Transactional
	@Query("UPDATE Account a SET a.passWord = :passWord " +
	       "WHERE a.userName = :userName " +
	       "AND EXISTS ( " +
	       "  SELECT p FROM Profile p " +
	       "  WHERE p.userNameProfile = :userName " +
	       "  AND p.phoneNumber = :phoneNumber)")
	 int forgotPassWord(String userName, String passWord, String phoneNumber);	 
}
