package com.tesla.parkingapp.controller;

import java.util.ArrayList;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.tesla.parkingapp.model.Parcare;
import com.tesla.parkingapp.model.User;
import com.tesla.parkingapp.service.ParcareService;
import com.tesla.parkingapp.service.UserService;
import com.tesla.parkingapp.web.UserRegistrationDto;

@Controller
public class AuthController {

	@Autowired
	private UserService userService;
	@Autowired
	private ParcareService parcareService;


	@RequestMapping(value = { "/login" }, method = RequestMethod.GET)
	public ModelAndView login() {
		ModelAndView model = new ModelAndView();

		model.setViewName("user/login");
		return model;
	}

	@RequestMapping(value = { "/registration" }, method = RequestMethod.GET)
	public ModelAndView showRegistration() {
		ModelAndView model = new ModelAndView();
		model.addObject("user", new UserRegistrationDto());
		model.setViewName("registration");

		return model;
	}

	@RequestMapping(value = { "/registration" }, method = RequestMethod.POST)
	public String registerUserAccount(@ModelAttribute("user") @Valid UserRegistrationDto userDto,
			BindingResult result) {

		User existing = userService.findUserByEmail(userDto.getEmail());
		if (existing != null) {
			result.rejectValue("email", null, "There is already an account registered with that email");
		}

		if (result.hasErrors()) {
			return "registration";
		}

		userService.saveUser(userDto);
		return "redirect:/registration?success";
	}

	@RequestMapping(value = { "/"}, method = RequestMethod.GET)
	public String home(HttpServletRequest request) {
		ModelAndView model = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		if (user != null) {
			Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();

			List<String> roles = new ArrayList<String>();

			for (GrantedAuthority a : authorities) {
				roles.add(a.getAuthority());
			}
			if (isAdmin(roles) || isFirma(roles)) {
				return "redirect:/admin";			
			} else if (isUser(roles)) {
				return "redirect:/user";
			}
		} 
		
		return "redirect:/login";
	

	}

	private boolean isUser(List<String> roles) {
		if (roles.contains("SITE_USER")) {
			return true;
		}
		return false;
	}

	private boolean isAdmin(List<String> roles) {
		if (roles.contains("ADMIN_USER")) {
			return true;
		}
		return false;
	}

	private boolean isFirma(List<String> roles) {
		if (roles.contains("FIRMA_USER")) {
			return true;
		}
		return false;
	}
}
