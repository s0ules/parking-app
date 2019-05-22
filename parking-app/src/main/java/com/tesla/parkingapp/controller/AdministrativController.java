package com.tesla.parkingapp.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;

import com.google.maps.model.LatLng;
import com.tesla.parkingapp.model.ObjChartProfit7Days;
import com.tesla.parkingapp.model.Parcare;
import com.tesla.parkingapp.model.Programare;
import com.tesla.parkingapp.model.ProgramareResponse;
import com.tesla.parkingapp.model.Statie;
import com.tesla.parkingapp.model.TipIncarcare;
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
	@Autowired
	private ParcareController parcareController;
	
	@RequestMapping(value = { "/admin" }, method = RequestMethod.GET)
	public ModelAndView admin(Authentication authentication) {
		ModelAndView model = new ModelAndView();
		
		boolean hasAdminRole = authentication.getAuthorities().stream()
		          .anyMatch(r -> r.getAuthority().equals("ADMIN_USER"));
		if (hasAdminRole) {
			model.addObject("parcari", parcareService.findAll());
		}else {
			User user = userService.findUserByEmail(authentication.getName());
			model.addObject("parcari", parcareService.findByUser_UserId(user.getId()));
		}
		
		model.addObject("parcare", new Parcare());
		model.setViewName("/admin");

		return model;

	}

	@RequestMapping(value = { "/", "/admin" }, params = "details", method = RequestMethod.POST)
	public RedirectView redirectToDetalii(@Valid Parcare parcare, BindingResult bindingResult,
			RedirectAttributes attributes) {

		Parcare p = parcareService.findById(parcare.getParcareId());
		attributes.addFlashAttribute("parcare", p);
		attributes.addFlashAttribute("locuri_ocupate", parcareController.getStatiiOcupate(parcare).size());
		return new RedirectView("/detalii-parcare");

		// return "redirect:/detalii-parcare";
	}

	@RequestMapping(value = "/detalii-parcare", method = RequestMethod.GET)
	public ModelAndView detalii_parcare(@Valid Parcare parcare, BindingResult bindingResult, Model model1,
			HttpSession session) {
		
		if (!bindingResult.hasErrors()) {
			session.setAttribute("parcare", parcare);
		}
		
		ModelAndView model = new ModelAndView();
		
		Parcare p = (Parcare) session.getAttribute("parcare");
		List<ObjChartProfit7Days> listChart = parcareController.getProfit7Days(p);
		List<Statie> statiiOcupate = parcareController.getStatiiOcupate(parcare);
		model.addObject("parcare", p);
		model.addObject("dataPointsList", listChart);
		model.addObject("locuri_ocupate", statiiOcupate.size());
		model1.addAttribute("parcare", p);
		model1.addAttribute("locuri_ocupate", statiiOcupate.size());
		model1.addAttribute("dataPointsList", parcareController.getProfit7Days(p));
		parcare = p;
		model.setViewName("/detalii-parcare");

		return model;

	}

	@RequestMapping(value = { "/", "/detalii-parcare" }, params = "update", method = RequestMethod.POST)
	public void adauga(@Valid Parcare parcare, BindingResult bindingResult, Model model,
			Authentication authentication) {

		Parcare p = parcareService.findById(parcare.getParcareId());
		p.setAdresa(parcare.getAdresa());
		p.setNr_locuri(parcare.getNr_locuri());
		p.setOraDeschidere(parcare.getOraDeschidere());
		p.setOraInchidere(parcare.getOraInchidere());
		p.setStatus(parcare.getStatus());

		parcareService.updateParcare(p);
		
		List<ObjChartProfit7Days> listChart = parcareController.getProfit7Days(p);
		model.addAttribute("dataPointsList", listChart);
		model.addAttribute("locuri_ocupate", parcareController.getStatiiOcupate(parcare).size());
		
		if (bindingResult.hasErrors())
			model.addAttribute("message", "failed");
		else
			model.addAttribute("message", "success");
		model.addAttribute("parcare", p);

	}

	@RequestMapping(value = { "/", "/detalii-parcare" }, params = "delete", method = RequestMethod.POST)
	public RedirectView delete(@Valid Parcare parcare, BindingResult bindingResult, RedirectAttributes attributes) {
		
		parcareService.deleteParcare(parcare.getParcareId());

		attributes.addFlashAttribute("parcari", parcareService.findAll());
		return new RedirectView("/");
		
	}
	
	@RequestMapping(value = { "/", "/detalii-parcare/changeAdministrator" }, method = RequestMethod.POST)
	public @ResponseBody String changeAdministrator(@RequestBody String email, BindingResult bindingResult, HttpSession session) {

		Parcare p = (Parcare)session.getAttribute("parcare");
		User administrator = userService.findUserByEmail(email);
		
		p.setUser(administrator);
		parcareService.updateParcare(p);
		ModelAndView model = new ModelAndView();
		if (bindingResult.hasErrors())
			return "failed";
		return "success";
	//	model.addObject("parcare", p);
		//model.setViewName("/detalii-parcare");
		//return model;
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
		return userService.search(request.getParameter("term"));
	}

	@RequestMapping(value = { "/adauga-parcare" }, params = "add", method = RequestMethod.POST)
	public ModelAndView creareParcare(@Valid Parcare parcare, BindingResult bindingResult,
			Authentication authentication) {
		User user = userService.findUserByEmail(authentication.getName());
		ModelAndView model = new ModelAndView();

		String adresa = parcare.getAdresa();
		if (!adresa.contains("RO"))
			adresa += adresa + ", RO";
		LatLng coord = Geolocation.getCoordinates(adresa);

		if (!parcareService.findByLatitudineAndLongitudine(coord.lat, coord.lng).isEmpty() && coord.lat != 0
				&& coord.lng != 0)
			bindingResult.rejectValue("adresa", null, "There is already a parking lot at this address");

		if (!bindingResult.hasErrors()) {
			parcare.setUser(user);
			parcare.setStatus(true);
			parcare.setLatitudine(coord.lat);
			parcare.setLongitudine(coord.lng);
			parcareService.saveParcare(parcare);
			int nr_locuri = parcare.getNr_locuri();
			for (int i = 0; i < nr_locuri; i++) {
				statieService.saveStatie(new Statie(parcare));
			}
			model.addObject("parcare", new Parcare());
			model.addObject("message", "success");
			
		}
		model.setViewName("/adauga-parcare");
		return model;
	}

	@RequestMapping(value = { "/", "/admin" }, params = "rezervations", method = RequestMethod.POST)
	public ModelAndView userReservations(@Valid Parcare parcare, BindingResult bindingResult) {
		ModelAndView model = new ModelAndView();
		List<Programare> rez = new ArrayList<>();
		List<Statie> statii = statieService.findByParcareId(parcare.getParcareId());

		for (Statie statie : statii) {
			List<Programare> progr = statie.getProgramari();

			LocalDateTime currentDate = LocalDateTime.now();
			for (Programare prog : progr) {
				if (prog.getOra_inceput().isAfter(currentDate)) {
					rez.add(new Programare(prog.getId_programare(), prog.getTip_incarcare(), prog.getOra_inceput(),
							prog.getOra_sfarsit(), prog.getStatie().getParcare().getAdresa(),
							prog.getNr_inmatriculare()));
				}
			}
		}

		model.addObject("rezervari", rez);
		model.setViewName("user/user-reservations");
		return model;

	}

}
