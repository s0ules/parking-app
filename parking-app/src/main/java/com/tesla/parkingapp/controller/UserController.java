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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.maps.model.LatLng;
import com.tesla.parkingapp.ParkingAppApplication;
import com.tesla.parkingapp.model.AvailableHoursStatie;
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

	@RequestMapping(value = { "/user" }, method = RequestMethod.GET)
	public ModelAndView user() {
		ModelAndView model = new ModelAndView();

		model.addObject("parcari", parcareService.findAll());
		model.setViewName("/user/user");

		return model;
	}

	@RequestMapping(value = { "/access_denied" }, method = RequestMethod.GET)
	public ModelAndView accessDenied() {
		ModelAndView model = new ModelAndView();
		model.setViewName("errors/access_denied");
		return model;
	}

	@RequestMapping(value = { "/user-reservations" }, method = RequestMethod.GET)
	public ModelAndView userReservations(Authentication authentication) {
		ModelAndView model = new ModelAndView();
		User user = userService.findUserByEmail(authentication.getName());
		List<Programare> progr = programareService.findByUser_UserId(user.getId());
		List<Programare> rez = new ArrayList<>();
		LocalDateTime currentDate = LocalDateTime.now();
		for (Programare prog : progr) {
			if (prog.getOra_inceput().isAfter(currentDate)) {
				rez.add(new Programare(prog.getId_programare(), prog.getTip_incarcare(), prog.getOra_inceput(),
						prog.getOra_sfarsit(), prog.getStatie().getParcare().getAdresa(), prog.getNr_inmatriculare()));
			}
		}
		if (user != null)
			model.addObject("rezervari", rez);
		model.setViewName("user/user-reservations");
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
		List<AvailableHoursStatie> availableHours = new ArrayList<>();
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
					dates.remove(LocalTime.of(h1, 00));
					// dates.remove(LocalTime.of(prog.getOra_sfarsit().getHour(), 00));
				}
			}
			availableHours.add(new AvailableHoursStatie(dates, statie.getStatieId()));
		}

		if (desiredHour.isFast_charger())
			return new HoursResponse(availableHours, getHoursFastCharging(
					parcareService.findById(desiredHour.getParcareId()), availableHours, desiredHour.getDate()));
		else
			return new HoursResponse(availableHours, getNoPreferenceHours(
					parcareService.findById(desiredHour.getParcareId()), availableHours, desiredHour.getDate()));
	}

	private List<StatieHour> getNoPreferenceHours(Parcare parcare, List<AvailableHoursStatie> avHoursStatii,
			LocalDate desiredDate) {
		List<StatieHour> hours = new ArrayList<>();
		LocalTime oraInchidere = parcare.getOraInchidere();
		LocalTime oraDeschidere = parcare.getOraDeschidere();
		LocalTime currentTime = LocalTime.now();
		LocalDate currentDate = LocalDate.now();
		LocalTime possibleHour = parcare.getOraDeschidere();
		boolean sameDay = false;

		if (currentDate.isEqual(desiredDate)) {
			if (currentTime.isAfter(LocalTime.of(00, 00))) {
				possibleHour = oraDeschidere;
			} else {
				possibleHour = LocalTime.of(currentTime.plusHours(1).getHour(), 00);
			}
			sameDay = true;
		}

		for (AvailableHoursStatie avHoursStatie : avHoursStatii) {
			for (LocalTime AvHourStatie : avHoursStatie.getHours()) {
				if ((possibleHour.isAfter(oraInchidere.minusHours(1)) && !sameDay)
						|| (LocalDateTime.of(currentDate, possibleHour)
								.isAfter(LocalDateTime.of(currentDate, oraInchidere)) && sameDay)) {
					return hours;
				}
				if (AvHourStatie.equals(possibleHour)) {
					hours.add(new StatieHour(possibleHour, avHoursStatie.getStatieId()));
					possibleHour = possibleHour.plusHours(1);
				}
			}
		}

		return hours;
	}

	private List<StatieHour> getHoursFastCharging(Parcare parcare, List<AvailableHoursStatie> avHoursStatii,
			LocalDate desiredDate) {
		List<StatieHour> hours = new ArrayList<>();
		LocalTime oraInchidere = parcare.getOraInchidere();
		LocalTime oraDeschidere = parcare.getOraDeschidere();
		LocalTime currentTime = LocalTime.now();
		LocalDate currentDate = LocalDate.now();
		LocalTime possibleHour = parcare.getOraDeschidere();
		boolean sameDay = false;

		if (currentDate.isEqual(desiredDate)) {
			if (currentTime.isAfter(LocalTime.of(00, 00))) {
				possibleHour = oraDeschidere;
			} else {
				possibleHour = LocalTime.of(currentTime.plusHours(1).getHour(), 00);
			}
			sameDay = true;
		}

		for (AvailableHoursStatie avHoursStatie : avHoursStatii) {
			for (LocalTime AvHourStatie : avHoursStatie.getHours()) {
				if ((possibleHour.isAfter(oraInchidere.minusHours(1)) && !sameDay)
						|| (LocalDateTime.of(currentDate, possibleHour)
								.isAfter(LocalDateTime.of(currentDate, oraInchidere)) && sameDay)) {
					return hours;
				}
				if (AvHourStatie.equals(possibleHour) && avHoursStatie.getHours().contains(possibleHour.plusHours(1))) {
					hours.add(new StatieHour(possibleHour, avHoursStatie.getStatieId()));
					possibleHour = possibleHour.plusHours(1);
				}
			}
		}

		return hours;
	}

	@RequestMapping(value = "/makeReservation", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody int makeReservation(@RequestBody ProgramareResponse data, Authentication authentication) {
		User user = userService.findUserByEmail(authentication.getName());
		int id_tip = 1;
		if (data.getFast_charger())
			id_tip = 2;
		TipIncarcare tip_incarcare = tipIncarcareService.findById(id_tip);
		Statie statie = statieService.findById(data.getId_statie());

		LocalDateTime ora_inceput = LocalDateTime.of(data.getDate(), data.getOra_inceput());
		int ora_sf = data.getOra_inceput().getHour() + 1;
		if (tip_incarcare.getTip_id() == 1)
			ora_sf = data.getOra_inceput().getHour() + 2;
		LocalTime sf_time = LocalTime.of(ora_sf, 00);
		LocalDateTime ora_sfarsit = LocalDateTime.of(data.getDate(), sf_time);
		Programare programare = new Programare(tip_incarcare, ora_inceput, ora_sfarsit, statie, user,
				data.getNr_inmatriculare());

		programareService.saveProgramare(programare);

		return data.getId_statie();
	}

}
