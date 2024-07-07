package com.boostmytool.healthForum.service;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boostmytool.healthForum.model.Account;

public interface AccountRepository extends JpaRepository<Account, String>{

}
