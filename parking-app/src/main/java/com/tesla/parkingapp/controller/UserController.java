package com.tesla.parkingapp.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.google.maps.model.LatLng;
import com.tesla.parkingapp.model.Coordinates;
import com.tesla.parkingapp.model.Parcare;
import com.tesla.parkingapp.model.User;
import com.tesla.parkingapp.service.ParcareService;
import com.tesla.parkingapp.service.UserService;
import com.tesla.parkingapp.utils.Geolocation;

@Controller
public class UserController {

 @Autowired
 private UserService userService;
 @Autowired
 private ParcareService parcareService;
 
 @RequestMapping(value= {"/", "/login"}, method=RequestMethod.GET)
 public ModelAndView login() {
  ModelAndView model = new ModelAndView();
  
  model.setViewName("user/login");
  return model;
 }
 
 /*@RequestMapping(value="/login", method=RequestMethod.POST, params="action=signup")
 public ModelAndView makeLogin() {
	 ModelAndView model = new ModelAndView();
	 model.setViewName("user/signup");
	 
	 return model;
 }*/

 @RequestMapping(value= {"/signup"}, method=RequestMethod.GET)
 public ModelAndView signup() {
  ModelAndView model = new ModelAndView();
  User user = new User();
  model.addObject("user", user);
  model.setViewName("user/signup");
  
  return model;
 }
 
 @RequestMapping(value= {"/signup"}, method=RequestMethod.POST)
 public ModelAndView createUser(@Valid User user, BindingResult bindingResult) {
  ModelAndView model = new ModelAndView();
  User userExists = userService.findUserByEmail(user.getEmail());
  
  if(userExists != null) {
   bindingResult.rejectValue("email", "error.user", "This email already exists!");
  }
  if(bindingResult.hasErrors()) {
   model.setViewName("user/signup");
  } else {
   userService.saveUser(user);
   model.addObject("msg", "User has been registered successfully!");
   model.addObject("user", new User());
   model.setViewName("user/signup");
  }
  
  return model;
 }
 
 @RequestMapping(value= {"/home"}, method=RequestMethod.GET)
 public ModelAndView home() {
  ModelAndView model = new ModelAndView();

  model.setViewName("/home");
  return model;
 }
 
 @RequestMapping(value= {"/user"}, method=RequestMethod.GET)
 public ModelAndView user() {
  ModelAndView model = new ModelAndView();
  Authentication auth = SecurityContextHolder.getContext().getAuthentication();
  User user = userService.findUserByEmail(auth.getName());
  
  model.addObject("userName", user.getEmail());
  
  model.addObject("parcari", parcareService.findAll());
  model.setViewName("/user/user");
  return model;
 }
 
 @RequestMapping(value= {"/admin"}, method=RequestMethod.GET)
 public ModelAndView admin() {
  ModelAndView model = new ModelAndView();
  
  Authentication auth = SecurityContextHolder.getContext().getAuthentication();
  User user = userService.findUserByEmail(auth.getName());
  
  model.addObject("userName", user.getEmail());
  model.setViewName("/admin");
  return model;
 }
 
 @RequestMapping(value="/administrare-parcare")
 public ModelAndView administrareParcare(){
	  ModelAndView model = new ModelAndView();
	  
	  Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	  User user = userService.findUserByEmail(auth.getName());
	  
	  model.addObject("userName", user.getEmail());
	  model.addObject("parcare", new Parcare());
	  model.addObject("parcari", parcareService.findAll());
	  model.setViewName("/administrare-parcare");
	  return model;
 }
 
 @RequestMapping(value="/administrare-parcare", params = "add", method=RequestMethod.POST)
 public void adauga(@Valid Parcare parcare, BindingResult bindingResult) {
	 LatLng coord = Geolocation.getCoordinates(parcare.getAdresa() + ", Cluj-Napoca, RO");
	 parcare.setStatus(true);
	 parcare.setLatitudine(coord.lat);
	 parcare.setLongitudine(coord.lng);
	 parcareService.saveParcare(parcare);
	// return "redirect:/administrare-parcare";
 }
 
 @RequestMapping(value="/administrare-parcare", params = "delete", method=RequestMethod.POST)
 public void delete(@Valid Parcare parcare, BindingResult bindingResult, Model model) {

	 parcareService.deleteParcare(parcare.getId_parcare());
	 model.addAttribute("parcari", parcareService.findAll());
	 //return "redirect:/administrare-parcare";
 }
 
 @RequestMapping(value= {"/access_denied"}, method=RequestMethod.GET)
 public ModelAndView accessDenied() {
  ModelAndView model = new ModelAndView();
  model.setViewName("errors/access_denied");
  return model;
 }
 
}
