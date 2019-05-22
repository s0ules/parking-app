package com.tesla.parkingapp.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.tesla.parkingapp.model.ObjChartProfit7Days;
import com.tesla.parkingapp.model.Parcare;
import com.tesla.parkingapp.model.Programare;
import com.tesla.parkingapp.model.Statie;
import com.tesla.parkingapp.service.StatieService;

@Controller
public class ParcareController {
	@Autowired
	private StatieService statieService;
	
	public List<Statie> getStatiiOcupate(Parcare parcare) {
		List<Statie> statiiOcupate = new ArrayList<Statie>();
		LocalDate currentDate = LocalDate.now();
		LocalTime currentTime = LocalTime.now();
		List<Statie> statii = statieService.findByParcareId(parcare.getParcareId());
		for (Statie statie : statii) {
			List<Programare> programari = statie.getProgramari();
			for (Programare programare : programari) {
				if (currentDate.isEqual(programare.getOra_inceput().toLocalDate()) && 
						currentTime.isAfter(programare.getOra_inceput().toLocalTime()) &&
						currentTime.isBefore(programare.getOra_sfarsit().toLocalTime())){
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
		for (Statie statie : statii) {
			List<Programare> programari = statie.getProgramari();
			for (Programare programare : programari) {
				if (programare.getStatie().getParcare().getParcareId() == parcare.getParcareId())
					if (programare.getOra_inceput().getDayOfMonth() == ziCurenta
							&& programare.getOra_inceput().getMonthValue() == lunaCurenta
							&& programare.getOra_inceput().getYear() == anCurent)
						if (programare.getTip_incarcare().getTip_id() == 2)
							profit += 3 * 60;
						else
							profit += 1 * 120;
			}
		}

		return profit;
	}
	
	public List<ObjChartProfit7Days> getProfit7Days(Parcare parcare){
		List<ObjChartProfit7Days> listObjChart = new ArrayList<>();
		LocalDate currentDate = LocalDate.now();
		for (int i = 0; i < 7; i++) {
			listObjChart.add(new ObjChartProfit7Days(currentDate, getProfitZi(parcare, currentDate)));
			currentDate = currentDate.minusDays(1);
		}
		return listObjChart;
		
	}
}
