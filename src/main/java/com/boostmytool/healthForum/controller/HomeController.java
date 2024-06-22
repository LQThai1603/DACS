package com.boostmytool.healthForum.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"home", "/home"})
public class HomeController {
	@GetMapping({"", "/"})
	public String showPostMainPage() {
		return "home/postMain";
	}
}
