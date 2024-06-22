package com.boostmytool.healthForum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.boostmytool.healthForum.service.AccountRepository;

@Controller
@RequestMapping("")
public class loginNavigation {
	@Autowired
	private AccountRepository aRepo;
	@GetMapping({"", "/"})
	public String showLoginPage() {
		
		return "redirect:/login";
	}
}
