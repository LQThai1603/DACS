package com.boostmytool.healthForum.controller;

import java.util.Optional;

import javax.naming.Binding;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.boostmytool.healthForum.model.Account;
import com.boostmytool.healthForum.model.AccountDto;
import com.boostmytool.healthForum.model.ForgotAccountDto;
import com.boostmytool.healthForum.model.Profile;
import com.boostmytool.healthForum.model.RegisterDto;
import com.boostmytool.healthForum.service.AccountRepository;
import com.boostmytool.healthForum.service.ProfileRepository;

import jakarta.validation.Valid;


@Controller
public class loginController {
	@Autowired
	private AccountRepository Arepo;
	@Autowired
	private ProfileRepository Prepo;
	private Account account;
	@GetMapping({"login", "/login"})
	public String showloginPage(Model model) {
		AccountDto accountDto = new AccountDto();
		model.addAttribute("accountDto", accountDto);
		return "login/index";
	}
	
	@PostMapping({"/login", "login"})
	public String login(
			@Valid @ModelAttribute AccountDto accountDto, 
			BindingResult result,
			RedirectAttributes redirectAttributes) {
		
		
		if(result.hasErrors()) {
			
			return "login/index";
		}
		
		account = Arepo.findById(accountDto.getUserName()).orElse(null);
		//kiểm tra tài khoản
		if (account == null) {
	        redirectAttributes.addFlashAttribute("error", "Tài khoản không tồn tại.");
	        return "redirect:/login";
	    }
	    // Kiểm tra mật khẩu	
	    if (!account.getPassWord().equals(accountDto.getPassWord())) {
	        redirectAttributes.addFlashAttribute("error", "Mật khẩu không đúng.");
	        return "redirect:/login";
	    }
	    
	    redirectAttributes.addAttribute("userName", account.getUserName());
   
        redirectAttributes.addFlashAttribute("success", "Đăng nhập thành công!");
        
        if(account.getUserName().equals("admin")) {
        	return "redirect:admin/start";
        }
		return "redirect:home/start";
	}
	
	@GetMapping({"/login/register", "login/register"})
	public String showRegisterPage(Model model) {
		RegisterDto registerDto = new RegisterDto();
		model.addAttribute("registerDto",registerDto);
		return "login/registerForm";
	}
	
	@PostMapping({"/login/register", "login/register"})
	public String registerAccount(
			Model model,
			@Valid @ModelAttribute RegisterDto registerDto, 
			BindingResult result,
			RedirectAttributes redirectAttributes){
		
		if(result.hasErrors()) {
			
			return "login/registerForm";
		}
		
		if(Arepo.findById(registerDto.getUserName()).orElse(null) != null) {
			redirectAttributes.addFlashAttribute("error", "tài khoản đã tồn tại");
			return "redirect:/login/register";
		}
		
		if(!registerDto.getPassWord().equals(registerDto.getConFirmPassWord())) {
			redirectAttributes.addFlashAttribute("error", "Xác nhận mật khẩu không đúng!");
			return "redirect:/login/register";
		}
		
		Account a = new Account(registerDto.getUserName(), registerDto.getPassWord(), registerDto.getUserName());
		Profile p = new Profile();
		p.setUserNameProfile(a.getUserName());
		Prepo.save(p);
		Arepo.save(a);
		return "redirect:/login";
	}
	
	@GetMapping({"/login/forgotAccount", "login/forgotAccount"})
	public String showForgotAccountPage(Model model) {
		ForgotAccountDto forgotAccountDto = new ForgotAccountDto();
		String phoneNumber = "";
		
		model.addAttribute("forgotAccountDto", forgotAccountDto);
		model.addAttribute("phoneNumber", phoneNumber);
		return "login/forgotAccount";
	}
	
	@PostMapping({"/login/forgotAccount", "login/forgotAccount"})
	public String updatePassWord(Model model, 
			@Valid @ModelAttribute ForgotAccountDto forgotAccountDto,
			BindingResult result, RedirectAttributes redirectAttributes) {
		model.addAttribute("registerDto", forgotAccountDto);
		if(result.hasErrors()) {
			System.out.println(result.getErrorCount());
			return "login/forgotAccount";
		}
		
		if(forgotAccountDto.getPassWord().equals(forgotAccountDto.getConFirmPassWord())) {
			Arepo.forgotPassWord(forgotAccountDto.getUserName(), forgotAccountDto.getPassWord(), forgotAccountDto.getPhoneNumber());
			return "redirect:/login";
		}
		
		return "login/forgotAccount";
	}
}
