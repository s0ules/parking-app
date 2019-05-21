package com.tesla.parkingapp.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;

import com.google.maps.model.LatLng;
import com.tesla.parkingapp.model.Parcare;
import com.tesla.parkingapp.model.Programare;
import com.tesla.parkingapp.model.Statie;
import com.tesla.parkingapp.model.User;
import com.tesla.parkingapp.service.CanvasjsChartService;
import com.tesla.parkingapp.service.ParcareService;
import com.tesla.parkingapp.service.StatieService;
import com.tesla.parkingapp.service.UserService;
import com.tesla.parkingapp.utils.Geolocation;

@Controller
public class AdministrativController {

	@Autowired
	private ParcareService parcareService;
	@Autowired
	private UserService userService;
	@Autowired
	private StatieService statieService;
	@Autowired
	private CanvasjsChartService canvasjsChartService;
	
	@RequestMapping(value = { "/admin" }, method = RequestMethod.GET)
	public ModelAndView admin() {
		ModelAndView model = new ModelAndView();

		model.addObject("parcare", new Parcare());
		model.addObject("parcari", parcareService.findAll());
		model.setViewName("/admin");

		return model;

	}

	@RequestMapping(value = "/firma", method = RequestMethod.GET)
	public ModelAndView firma(Authentication authentication) {
		
		User user = userService.findUserByEmail(authentication.getName());
		ModelAndView model = new ModelAndView();

		model.addObject("parcare", new Parcare());
		model.addObject("parcari", parcareService.findByUser_UserId(user.getId()));
		model.setViewName("/firma");

		return model;

	}

	/*
	 * @RequestMapping(value = {"/", "/admin"}, params = "details", method =
	 * RequestMethod.POST) public RedirectView redirectToDetalii(@Valid Parcare
	 * parcare, RedirectAttributes attributes) {
	 * attributes.addFlashAttribute("parcare", parcare); return new
	 * RedirectView("/detalii-parcare"); }
	 */

	@RequestMapping(value = { "/", "/admin" }, params = "details", method = RequestMethod.POST)
	public RedirectView redirectToDetalii(@Valid Parcare parcare, BindingResult bindingResult,
			RedirectAttributes attributes) {
		Parcare p = parcareService.findById(parcare.getParcareId());
		attributes.addFlashAttribute("parcare", p);
		attributes.addFlashAttribute("locuri_ocupate", getStatiiOcupate(parcare).size());
		return new RedirectView("/detalii-parcare");

		// return "redirect:/detalii-parcare";
	}

	@RequestMapping(value = "/detalii-parcare", method = RequestMethod.GET)
	public ModelAndView detalii_parcare(@Valid Parcare parcare, Model model1) {
		LocalDate ld = LocalDate.of(2019, 05, 13);
		System.out.println(ld);
		System.out.println(getProfitZi(parcare, LocalDate.of(2019, 05, 13)));
		System.out.println(getStatiiOcupate(parcare).size());
		List<List<Map<Object, Object>>> canvasjsDataList = canvasjsChartService.getCanvasjsChartData();
		
		ModelAndView model = new ModelAndView();
		model.addObject("dataPointsList", canvasjsDataList);
		if (!model1.containsAttribute("parcare"))
			model.addObject("parcare", new Parcare());
		model.setViewName("/detalii-parcare");

		return model;

	}

	@RequestMapping(value = { "/", "/detalii-parcare" }, params = "update", method = RequestMethod.POST)
	public void adauga(@Valid Parcare parcare, BindingResult bindingResult, Model model, Authentication authentication) {

		// LatLng coord = Geolocation.getCoordinates(parcare.getAdresa());
		// parcare.setLatitudine(coord.lat);
		// parcare.setLongitudine(coord.lng);

		// int locuri = parcare.getNr_locuri();

		/*
		 * for (int i = 0; i < locuri; i++) { Statie statie = new Statie();
		 * statie.setParcare(parcare); statieService.saveStatie(statie);
		 * //parcare.addStatie(statie); }
		 */
		Parcare p = parcareService.findById(parcare.getParcareId());
		p.setAdresa(parcare.getAdresa());
		p.setNr_locuri(parcare.getNr_locuri());
		p.setOraDeschidere(parcare.getOraDeschidere());
		p.setOraInchidere(parcare.getOraInchidere());
		p.setStatus(parcare.getStatus());

		parcareService.updateParcare(p);

		if (bindingResult.hasErrors())
			model.addAttribute("message", "failed");
		else
			model.addAttribute("message", "success");
		model.addAttribute("parcare", p);

		// model.addAttribute("parcari", parcareService.findAll());

		// return "redirect:/administrare-parcare";
	}

	@RequestMapping(value = { "/", "/detalii-parcare" }, params = "delete", method = RequestMethod.POST)
	public ModelAndView delete(@Valid Parcare parcare, BindingResult bindingResult) {
		ModelAndView model = new ModelAndView();
		parcareService.deleteParcare(parcare.getParcareId());
		model.addObject("parcari", parcareService.findAll());
		model.setViewName("/admin");
		return model;
	}

	@RequestMapping(value = { "/adauga-parcare" }, method = RequestMethod.GET)
	public ModelAndView showCreareParcare() {
		ModelAndView model = new ModelAndView();
		model.addObject("parcare", new Parcare());
		model.setViewName("/adauga-parcare");
		return model;
	}

	@RequestMapping(value = "/detalii-parcare/search", method = RequestMethod.GET)
	@ResponseBody
	public List<String> search(HttpServletRequest request) {
		System.out.println(request.getParameter("term"));
		return userService.search(request.getParameter("term"));
	}
	
	@RequestMapping(value = { "/adauga-parcare" }, params = "add", method = RequestMethod.POST)
	public ModelAndView creareParcare(@Valid Parcare parcare, BindingResult bindingResult, Authentication authentication) {
		User user = userService.findUserByEmail(authentication.getName());	
		ModelAndView model = new ModelAndView();
		
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

		List<String> roles = new ArrayList<String>();

		for (GrantedAuthority a : authorities) {
			roles.add(a.getAuthority());
		}
		if (roles.contains("ADMIN_USER")) 
			model.setViewName("/admin");
		else 
			model.setViewName("/firma");

		String adresa = parcare.getAdresa();
		if (!adresa.contains("RO"))
			adresa += adresa + ", RO";	
		LatLng coord = Geolocation.getCoordinates(adresa);


		if (!parcareService.findByLatitudineAndLongitudine(coord.lat, coord.lng).isEmpty() && coord.lat != 0 && coord.lng != 0) 
			bindingResult.rejectValue("adresa", null, "There is already a parking lot at this address");

		if (bindingResult.hasErrors()) {
			model.setViewName("/adauga-parcare");
		}else {
			parcare.setUser(user);
			parcare.setStatus(true);
			parcare.setLatitudine(coord.lat);
			parcare.setLongitudine(coord.lng);
			parcareService.saveParcare(parcare);
		}
		
		return model;
	}
	
	public List<Statie> getStatiiOcupate(Parcare parcare) {
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

	public int getProfitZi(Parcare parcare, LocalDate date) {
		int profit = 0;
		int ziCurenta = date.getDayOfMonth();
		int lunaCurenta = date.getMonthValue();
		int anCurent = date.getYear();
		
		List<Statie> statii = statieService.findByParcareId(parcare.getParcareId());
		for(Statie statie : statii) {
			List<Programare> programari = statie.getProgramari();
			for(Programare programare : programari) {
				if(programare.getStatie().getParcare().getParcareId() == parcare.getParcareId())
					if(programare.getOra_inceput().getDayOfMonth() == ziCurenta && programare.getOra_inceput().getMonthValue() == lunaCurenta && programare.getOra_inceput().getYear() ==
					anCurent)
						if(programare.getTip_incarcare().getTip_id() == 2)
							profit+=3*60;
						else
							profit+=1*120;
			}
		}
		
		return profit;
	}

	@RequestMapping(value = { "/", "/admin" }, params = "rezervations", method = RequestMethod.POST)
	public ModelAndView userReservations(@Valid Parcare parcare) {
		ModelAndView model = new ModelAndView();
		
		List<Programare> rez = new ArrayList<>();
		List<Statie> statii = statieService.findByParcareId(parcare.getParcareId());
		
		for (Statie statie : statii) {
			List<Programare> progr = statie.getProgramari();
			
			LocalDateTime currentDate = LocalDateTime.now();
			for (Programare prog : progr) {
				if (prog.getOra_inceput().isAfter(currentDate)) {
					rez.add(new Programare(prog.getId_programare(), prog.getTip_incarcare(), prog.getOra_inceput(),
							prog.getOra_sfarsit(), prog.getStatie().getParcare().getAdresa(), prog.getNr_inmatriculare()));
				}
			}
		}
		
		model.addObject("rezervari", rez);
		model.setViewName("user/user-reservations");
		return model;

	}
	
}
