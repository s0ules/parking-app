package com.tesla.parkingapp.controller;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.format.annotation.DateTimeFormat;
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
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.maps.model.LatLng;
import com.tesla.parkingapp.ParkingAppApplication;
import com.tesla.parkingapp.model.AvailableHours;
import com.tesla.parkingapp.model.DesiredReservationHour;
import com.tesla.parkingapp.model.HoursResponse;
import com.tesla.parkingapp.model.MyResponse;
import com.tesla.parkingapp.model.Parcare;
import com.tesla.parkingapp.model.Programare;
import com.tesla.parkingapp.model.ProgramareResponse;
import com.tesla.parkingapp.model.Statie;
import com.tesla.parkingapp.model.StatieHour;
import com.tesla.parkingapp.model.TipIncarcare;
import com.tesla.parkingapp.model.User;
import com.tesla.parkingapp.service.ParcareService;
import com.tesla.parkingapp.service.ParcareServiceImpl;
import com.tesla.parkingapp.service.ProgramareService;
import com.tesla.parkingapp.service.StatieService;
import com.tesla.parkingapp.service.TipIncarcareService;
import com.tesla.parkingapp.service.UserService;
import com.tesla.parkingapp.utils.Geolocation;

@Controller
public class UserController {

	@Autowired
	private TipIncarcareService tipIncarcareService;
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

	@RequestMapping(value = { "/user" }, method = RequestMethod.GET)
	public ModelAndView user() {
		ModelAndView model = new ModelAndView();

		model.addObject("parcari", parcareService.findAll());
		model.setViewName("/user/user");

		return model;
	}

	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public ModelAndView administrareParcare() {
		ModelAndView model = new ModelAndView();

		model.addObject("parcare", new Parcare());
		model.addObject("parcari", parcareService.findAll());
		model.setViewName("/admin");

		return model;

	}

	@RequestMapping(value = "/admin", params = "update", method = RequestMethod.POST)
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

	@RequestMapping(value = "/admin", params = "delete", method = RequestMethod.POST)
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

	@RequestMapping(value = { "/creare-parcare" }, method = RequestMethod.GET)
	public ModelAndView creareParcare() {
		ModelAndView model = new ModelAndView();
		model.setViewName("/creare-parcare");
		return model;
	}

	private List<LocalTime> getPossibleHours(Parcare p) {

		List<LocalTime> dates = new ArrayList<>();
		int start_hour = p.getOraDeschidere().getHour();
		int end_hour = p.getOraInchidere().getHour();
		for (int i = start_hour; i < end_hour; i++) {
			dates.add(LocalTime.of(i, 00));
		}
		return dates;
	}

	@RequestMapping(value = "/getAvailableHours", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody HoursResponse getAvailableHours(@RequestBody DesiredReservationHour desiredHour) {

		List<AvailableHours> availableHours = new ArrayList<>();
		List<Statie> statii = statieService.findByParcareId(desiredHour.getParcareId());
		for (Statie statie : statii) {
			List<LocalTime> dates = getPossibleHours(parcareService.findById(desiredHour.getParcareId()));
			List<Programare> lp = programareService.findByStatie_StatieId(statie.getStatieId());

			for (Programare prog : lp) {
				if (prog.getOra_inceput().getMonthValue() == desiredHour.getDate().getMonth().getValue()
						&& prog.getOra_inceput().getYear() == desiredHour.getDate().getYear()
						&& prog.getOra_inceput().getDayOfMonth() == desiredHour.getDate().getDayOfMonth()) {
					int h1 = prog.getOra_inceput().getHour();
					int h2 = prog.getOra_sfarsit().getHour();
					if (h2 - h1 == 2) {
						dates.remove(LocalTime.of(h1 + 1, 00));
					}
					dates.remove(LocalTime.of(prog.getOra_inceput().getHour(), 00));
					dates.remove(LocalTime.of(prog.getOra_sfarsit().getHour(), 00));
				}
			}
			availableHours.add(new AvailableHours(dates, statie.getStatieId()));
		}
		
		return new HoursResponse(availableHours, getNoPreferenceHours(parcareService.findById(desiredHour.getParcareId()), availableHours));
	}

	private List<StatieHour> getNoPreferenceHours(Parcare parcare, List<AvailableHours> avHours) {
		List<StatieHour> hours = new ArrayList<>();
		LocalTime oraDeschidere = parcare.getOraDeschidere();
		LocalTime oraInchidere = parcare.getOraInchidere();
		while (oraDeschidere.isBefore(oraInchidere)) {
			System.out.println(oraDeschidere);
			for (AvailableHours avHour : avHours) {
				if (avHour.getHours().contains(oraDeschidere)) {
					hours.add(new StatieHour(oraDeschidere, avHour.getStatieId()));
					oraDeschidere = oraDeschidere.plusHours(1);
					break;
				}
			}
		}
		return hours;
	}

	@RequestMapping(value = "/makeReservation", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String makeReservation(@RequestBody ProgramareResponse data,
			@SessionAttribute("user") String useremail) {

		TipIncarcare tip_incarcare = tipIncarcareService.findById(data.getFast_charger());
		Statie statie = statieService.findById(data.getId_statie());
		User user = userService.findUserByEmail(useremail);
		LocalDateTime ora_inceput = LocalDateTime.of(data.getDate(), data.getOra_inceput());
		int ora_sf = data.getOra_inceput().getHour() + 1;
		if (tip_incarcare.getTip_id() == 2)
			ora_sf = data.getOra_inceput().getHour() + 2;
		LocalTime sf_time = LocalTime.of(ora_sf, 00);
		LocalDateTime ora_sfarsit = LocalDateTime.of(data.getDate(), sf_time);
		Programare programare = new Programare(tip_incarcare, ora_inceput, ora_sfarsit, statie, user);

		programareService.saveProgramare(programare);

		return "success";
	}

	public List<Statie> returnareStatiiOcupate(Parcare parcare) {
		List<Statie> statiiOcupate = new ArrayList<Statie>();
		int oraIncepere = LocalTime.now().getHour();
		int ziCurenta = LocalDate.now().getDayOfMonth();
		int lunaCurenta = LocalDate.now().getMonthValue();
		int anCurent = LocalDate.now().getYear();

		List<Statie> statii = statieService.findByParcareId(parcare.getParcareId());
		for (Statie statie : statii) {
			List<Programare> programari = statie.getProgramari();
			for (Programare programare : programari) {
				if (programare.getOra_inceput().getDayOfMonth() == ziCurenta
						&& programare.getOra_inceput().getMonthValue() == lunaCurenta
						&& programare.getOra_inceput().getYear() == anCurent
						&& programare.getOra_inceput().getHour() == oraIncepere
						&& parcare.getParcareId() == programare.getStatie().getParcare().getParcareId()) {
					statiiOcupate.add(programare.getStatie());
				}
			}
		}

		return statiiOcupate;
	}

	public static void main(String[] args) {

	}
}
