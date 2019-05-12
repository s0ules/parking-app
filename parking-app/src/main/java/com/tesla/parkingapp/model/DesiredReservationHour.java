package com.tesla.parkingapp.model;

import java.time.LocalDate;

public class DesiredReservationHour {

	private LocalDate date;
	private int parcareId;

	public DesiredReservationHour() {
		
	}
	
	public DesiredReservationHour(LocalDate date, int statieId) {
		super();
		this.date = date;
		this.parcareId = statieId;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public int getParcareId() {
		return parcareId;
	}

	public void setParcareId(int parcareId) {
		this.parcareId = parcareId;
	}
	
	
}
