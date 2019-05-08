package com.tesla.parkingapp.controller;

import java.sql.Date;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.maps.model.LatLng;
import com.tesla.parkingapp.model.AvailableHour;
import com.tesla.parkingapp.model.MyResponse;
import com.tesla.parkingapp.model.Parcare;
import com.tesla.parkingapp.model.Programare;
import com.tesla.parkingapp.model.Statie;
import com.tesla.parkingapp.model.User;
import com.tesla.parkingapp.service.ParcareService;
import com.tesla.parkingapp.service.ProgramareService;
import com.tesla.parkingapp.service.StatieService;
import com.tesla.parkingapp.service.UserService;
import com.tesla.parkingapp.utils.Geolocation;

@Controller
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private ParcareService parcareService;
	@Autowired
	private StatieService statieService;
	@Autowired
	private ProgramareService programareService;
	@Autowired
	private User user;

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
	public ModelAndView createUser(@Valid User user, BindingResult bindingResult) {
		ModelAndView model = new ModelAndView();
		User userExists = userService.findUserByEmail(user.getEmail());

		if (userExists != null) {
			bindingResult.rejectValue("email", "error.user", "This email already exists!");
		}
		if (bindingResult.hasErrors()) {
			model.setViewName("user/signup");
		} else {
			userService.saveUser(user);
			model.addObject("msg", "User has been registered successfully!");
			model.addObject("user", new User());
			model.setViewName("user/signup");
		}

		return model;
	}

	@RequestMapping(value = { "/", "/home" }, method = RequestMethod.GET)
	public ModelAndView home(HttpServletRequest request) {
		ModelAndView model = new ModelAndView();
		System.out.println(request.getSession().getAttribute("user"));
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		if (user != null) {
			Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();

			List<String> roles = new ArrayList<String>();

			for (GrantedAuthority a : authorities) {
				roles.add(a.getAuthority());
			}
			if (isAdmin(roles)) {
				model.setViewName("/admin");
			} else if (isUser(roles)) {
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

	@RequestMapping(value = { "/user" }, method = RequestMethod.GET)
	public ModelAndView user() {
		ModelAndView model = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());

		model.addObject("userName", user.getEmail());

		model.addObject("parcari", parcareService.findAll());
		model.setViewName("/user/user");
		return model;
	}

	@RequestMapping(value = { "/admin" }, method = RequestMethod.GET)
	public ModelAndView admin(HttpServletRequest request) {
		ModelAndView model = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		user = userService.findUserByEmail(auth.getName());

		model.addObject("userName", user.getEmail());
		model.setViewName("/admin");
		return model;
	}

	@RequestMapping(value = "/administrare-parcare", method = RequestMethod.GET)
	public ModelAndView administrareParcare() {
		ModelAndView model = new ModelAndView();

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());

		model.addObject("userName", user.getEmail());
		model.addObject("parcare", new Parcare());

		model.addObject("parcari", parcareService.findAll());
		List<Parcare> p =  parcareService.findAll();
		Parcare parcare = p.get(0);
		List<Statie> s = statieService.findByParcareId(parcare.getParcareId());
		for (Statie statie : s) {
			List<Programare> prog = statie.getProgramari();
			for (Programare p1 : prog) {
				
			}
		}

		model.setViewName("/administrare-parcare");
		return model;

	}

	@RequestMapping(value = "/administrare-parcare", params = "update", method = RequestMethod.POST)
	public void adauga(@Valid Parcare parcare, BindingResult bindingResult, Model model) {

		LatLng coord = Geolocation.getCoordinates(parcare.getAdresa() + ", Cluj-Napoca, RO");
		parcare.setStatus(true);
		parcare.setLatitudine(coord.lat);
		parcare.setLongitudine(coord.lng);

		int locuri = parcare.getNr_locuri();

		for (int i = 0; i < locuri; i++) {
			Statie statie = new Statie();
			statie.setParcare(parcare);
			statieService.saveStatie(statie);
			// parcare.addStatie(statie);
		}

		parcareService.updateParcare(parcare);

		model.addAttribute("parcare", new Parcare());

		model.addAttribute("parcari", parcareService.findAll());

		// return "redirect:/administrare-parcare";
	}

	@RequestMapping(value = "/administrare-parcare", params = "delete", method = RequestMethod.POST)
	public void delete(@Valid Parcare parcare, BindingResult bindingResult, Model model) {

		parcareService.deleteParcare(parcare.getParcareId());
		model.addAttribute("parcari", parcareService.findAll());
		// return "redirect:/administrare-parcare";
	}

	@RequestMapping(value = { "/access_denied" }, method = RequestMethod.GET)
	public ModelAndView accessDenied() {
		ModelAndView model = new ModelAndView();
		model.setViewName("errors/access_denied");
		return model;
	}

	@RequestMapping(value = "/getAvailableHours", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<LocalTime> getAvailableHours(@RequestBody AvailableHour avHour) {
		System.out.println(avHour.getStatieId());
		Statie statie = statieService.findByParcareId(avHour.getStatieId()).get(1);

		List<Programare> lp = programareService.findByStatie_StatieId(statie.getStatieId());
		System.out.println(lp.get(0).getOra_inceput().toString());
		
		List<LocalTime> dates= new ArrayList<>();
		LocalTime t1 = LocalTime.of(8, 00);
		dates.add(t1);
		return dates;
	}
}
