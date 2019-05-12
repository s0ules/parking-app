package com.tesla.parkingapp.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class ProgramareResponse {

	private int fast_charger;
	private int id_statie;
	private LocalDate date;
	private LocalTime ora_inceput;

	public ProgramareResponse() {

	}

	public ProgramareResponse(Boolean fast_charger, int id_statie, LocalDate date, LocalTime ora_inceput) {
		super();
		if (fast_charger)
			this.fast_charger = 2;
		else
			this.fast_charger = 1;
		this.id_statie = id_statie;
		this.date = date;
		this.ora_inceput = ora_inceput;
	}

	public int getFast_charger() {
		return fast_charger;
	}

	public void setFast_charger(Boolean fast_charger) {
		if (fast_charger)
			this.fast_charger = 2;
		else
			this.fast_charger = 1;
	}

	public int getId_statie() {
		return id_statie;
	}

	public void setId_statie(int id_statie) {
		this.id_statie = id_statie;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public LocalTime getOra_inceput() {
		return ora_inceput;
	}

	public void setOra_inceput(LocalTime ora_inceput) {
		this.ora_inceput = ora_inceput;
	}

}
