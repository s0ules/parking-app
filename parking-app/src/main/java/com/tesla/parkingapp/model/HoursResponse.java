package com.tesla.parkingapp.model;

import java.util.List;

public class HoursResponse {
	private List<AvailableHours> availableHours;
	private List<StatieHour> availableHoursNoPreference;
	
	public HoursResponse() {
		
	}
	
	public HoursResponse(List<AvailableHours> availableHours, List<StatieHour> availableHoursNoPreference) {
		super();
		this.availableHours = availableHours;
		this.availableHoursNoPreference = availableHoursNoPreference;
	}

	public List<AvailableHours> getAvailableHours() {
		return availableHours;
	}

	public void setAvailableHours(List<AvailableHours> availableHours) {
		this.availableHours = availableHours;
	}

	public List<StatieHour> getAvailableHoursNoPreference() {
		return availableHoursNoPreference;
	}

	public void setAvailableHoursNoPreference(List<StatieHour> availableHoursNoPreference) {
		this.availableHoursNoPreference = availableHoursNoPreference;
	}
	
	
}
