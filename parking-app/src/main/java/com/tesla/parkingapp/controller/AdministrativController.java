package com.tesla.parkingapp.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.google.maps.model.LatLng;
import com.tesla.parkingapp.model.Parcare;
import com.tesla.parkingapp.model.Statie;
import com.tesla.parkingapp.service.ParcareService;
import com.tesla.parkingapp.service.StatieService;
import com.tesla.parkingapp.utils.Geolocation;

@Controller
public class AdministrativController {

	@Autowired
	private ParcareService parcareService;
	@Autowired
	private StatieService statieService;
	
	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public ModelAndView admin() {
		ModelAndView model = new ModelAndView();

		model.addObject("parcare", new Parcare());
		model.addObject("parcari", parcareService.findAll());
		model.setViewName("/admin");

		return model;

	}

	@RequestMapping(value = "/firma", method = RequestMethod.GET)
	public ModelAndView firma() {
		ModelAndView model = new ModelAndView();

		model.addObject("parcare", new Parcare());
		model.addObject("parcari", parcareService.findById(0));
		model.setViewName("/firma");

		return model;

	}
	
	@RequestMapping(value = "/admin", params = "details", method = RequestMethod.POST)
	public RedirectView redirectToDetalii(RedirectAttributes attributes) {	
		attributes.addFlashAttribute("flashAttribute", "redirectWithRedirectAttributes");
	    return new RedirectView("/detalii-parcare");
	}
		
	@RequestMapping(value = "/detalii-parcare", method = RequestMethod.GET)
	public ModelAndView detalii_parcare() {
		ModelAndView model = new ModelAndView();

		model.addObject("parcare", new Parcare());
		model.setViewName("/detalii-parcare");

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
	
	@RequestMapping(value = { "/creare-parcare" }, method = RequestMethod.GET)
	public ModelAndView creareParcare() {
		ModelAndView model = new ModelAndView();
		model.setViewName("/creare-parcare");
		return model;
	}


	
}
