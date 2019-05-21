package com.tesla.parkingapp.model;

import java.time.LocalDate;

public class DesiredReservationHour {

	private boolean fast_charger;
	private LocalDate date;
	private int parcareId;

	public DesiredReservationHour() {
		
	}
	
	public DesiredReservationHour(boolean fast_charger, LocalDate date, int statieId) {
		super();
		this.fast_charger = fast_charger;
		this.date = date;
		this.parcareId = statieId;
	}
	
	public boolean isFast_charger() {
		return fast_charger;
	}

	public void setFast_charger(boolean fast_charger) {
		this.fast_charger = fast_charger;
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
