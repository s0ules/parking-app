package com.tesla.parkingapp.model;

import java.time.LocalTime;

public class StatieHour {
	private LocalTime hour;
	private int id_statie;
	
	public StatieHour() {
		
	}
	
	public StatieHour(LocalTime hour, int id_statie) {
		super();
		this.hour = hour;
		this.id_statie = id_statie;
	}
	public LocalTime getHour() {
		return hour;
	}
	public void setHour(LocalTime hour) {
		this.hour = hour;
	}
	public int getId_statie() {
		return id_statie;
	}
	public void setId_statie(int id_statie) {
		this.id_statie = id_statie;
	}
	
	
}
