package com.tesla.parkingapp.model;

import java.sql.Date;

public class AvailableHour {

	private Date date;
	private int statieId;

	public AvailableHour() {
		
	}
	
	public AvailableHour(Date date, int statieId) {
		super();
		this.date = date;
		this.statieId = statieId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getStatieId() {
		return statieId;
	}

	public void setStatieId(int statieId) {
		this.statieId = statieId;
	}
	
	
}
