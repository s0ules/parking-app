package com.tesla.parkingapp.model;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AvailableHoursStatie {

	private List<LocalTime> hours;
	private int statieId;
	
	public AvailableHoursStatie() {
		
	}
	
	public AvailableHoursStatie(List<LocalTime> hours, int statieId) {
		super();
		this.hours = hours;
		this.statieId = statieId;
	}

	public List<LocalTime> getHours() {
		return hours;
	}

	public void setHours(List<LocalTime> hours) {
		this.hours = new ArrayList<>(hours);
	}

	public int getStatieId() {
		return statieId;
	}

	public void setStatieId(int statieId) {
		this.statieId = statieId;
	}
	
}
