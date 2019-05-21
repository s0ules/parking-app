package com.tesla.parkingapp.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class ProgramareResponse {

	private boolean fast_charger;
	private int id_statie;
	private LocalDate date;
	private LocalTime ora_inceput;
	private String nr_inmatriculare;
	public ProgramareResponse() {

	}

	public ProgramareResponse(boolean fast_charger, int id_statie, LocalDate date, LocalTime ora_inceput, String nr_inmatriculare) {
		super();
		this.fast_charger = fast_charger;
		this.id_statie = id_statie;
		this.date = date;
		this.ora_inceput = ora_inceput;
		this.nr_inmatriculare = nr_inmatriculare;
	}

	public boolean getFast_charger() {
		return fast_charger;
	}

	public void setFast_charger(boolean fast_charger) {
		this.fast_charger = fast_charger;
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

	public String getNr_inmatriculare() {
		return nr_inmatriculare;
	}

	public void setNr_inmatriculare(String nr_inmatriculare) {
		this.nr_inmatriculare = nr_inmatriculare;
	}

	
}
