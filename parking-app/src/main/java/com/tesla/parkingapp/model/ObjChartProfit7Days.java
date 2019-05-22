package com.tesla.parkingapp.model;

import java.time.LocalDate;

public class ObjChartProfit7Days {
	private LocalDate x;
	private double y;
	
	
	public ObjChartProfit7Days() {
	
	}
	
	public ObjChartProfit7Days(LocalDate x, double y) {
		super();
		this.x = x;
		this.y = y;
	}
	public LocalDate getX() {
		return x;
	}
	public void setX(LocalDate x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	
	
}
