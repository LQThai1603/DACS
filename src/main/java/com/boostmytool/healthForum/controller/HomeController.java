package com.boostmytool.healthForum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.boostmytool.healthForum.model.Account;
import com.boostmytool.healthForum.service.AccountRepository;

@Controller
@RequestMapping({"home", "/home"})
public class HomeController {
	private Account account = null;
	@Autowired
	private AccountRepository Arepo;
	@GetMapping({"start"})
	public String showPostMainPage(@RequestParam String userName) {
		return "home/postMain";
	}
	@GetMapping({"", "/"})
	public String showPostMainPage() {
		System.out.println(account.getPassWord());
		return "home/postMain";
	}
}
