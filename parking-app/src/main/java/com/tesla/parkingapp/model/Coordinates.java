package com.tesla.parkingapp.model;

public class Coordinates {
	private Double lat;
	private Double lng;
	
	public Coordinates() {
		
	}
	
	public Coordinates(Double lat, Double lng) {
		super();
		this.lat = lat;
		this.lng = lng;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}
	
	
}
