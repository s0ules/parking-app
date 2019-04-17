package com.tesla.parkingapp.utils;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.Geometry;
import com.google.maps.model.LatLng;
import com.tesla.parkingapp.model.Coordinates;

public class Geolocation {
	
	public static void main(String[] args) {
		getCoordinates("Calea Motilor 60, Cluj-Napoca, RO");
	}
	
	public static LatLng getCoordinates(String address) {
		Coordinates coord = new Coordinates();
		GeoApiContext context = new GeoApiContext.Builder()
			    .apiKey("AIzaSyDvoYpTH5xFcgsi8CW6Gh7eimOd6AoOIUY")
			    .build();
			GeocodingResult[] results;
			try {
				results = GeocodingApi.geocode(context,
				    address).await();
				
				return results[0].geometry.location;
				
				
			} catch (ApiException | InterruptedException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
			

	}
}
