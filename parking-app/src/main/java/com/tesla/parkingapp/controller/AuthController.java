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
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.tesla.parkingapp.model.Parcare;
import com.tesla.parkingapp.model.User;
import com.tesla.parkingapp.service.ParcareService;
import com.tesla.parkingapp.service.UserService;
import com.tesla.parkingapp.validator.UserValidator;

@Controller
public class AuthController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private ParcareService parcareService;
	@Autowired
	private UserValidator userValidator;
	
	@RequestMapping(value = { "/login" }, method = RequestMethod.GET)
	public ModelAndView login() {
		ModelAndView model = new ModelAndView();

		model.setViewName("user/login");
		return model;
	}

	@RequestMapping(value = { "/signup" }, method = RequestMethod.GET)
	public ModelAndView signup() {
		ModelAndView model = new ModelAndView();
		User user = new User();
		model.addObject("user", user);
		model.setViewName("user/signup");

		return model;
	}

	@RequestMapping(value = { "/signup" }, method = RequestMethod.POST)
	public ModelAndView createUser(@Valid User user, BindingResult bindingResult, ModelAndView model) {
		 userValidator.validate(user, bindingResult);
		 model.setViewName("user/signup");
		    if (bindingResult.hasErrors()) {
		        return model;
		    }
		/*User userExists = userService.findUserByEmail(user.getEmail());

		if (userExists != null) {
			bindingResult.rejectValue("email", "error.user", "This email already exists!");
		}
		if (bindingResult.hasErrors()) {
			model.setViewName("user/signup");
		} else {*/
			userService.saveUser(user);
			model.addObject("msg", "User has been registered successfully!");
			model.addObject("user", new User());
			model.setViewName("user/signup");
	//	}

		return model;
	}
	
	@RequestMapping(value = { "/", "/home" }, method = RequestMethod.GET)
	public ModelAndView home(HttpServletRequest request) {
		ModelAndView model = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		if (user != null) {
			Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();

			List<String> roles = new ArrayList<String>();

			for (GrantedAuthority a : authorities) {
				roles.add(a.getAuthority());
			}
			if (isAdmin(roles)) {
				model.addObject("parcare", new Parcare());
				model.addObject("parcari", parcareService.findAll());
				model.setViewName("/admin");
			} else if (isUser(roles)) {
				model.addObject("parcari", parcareService.findAll());
				model.setViewName("/user");
			}
		} else {
			model.setViewName("user/login");
		}

		return model;

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
