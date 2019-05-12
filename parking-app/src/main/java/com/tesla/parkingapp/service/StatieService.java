package com.tesla.parkingapp.service;

import java.util.List;

import com.tesla.parkingapp.model.Statie;

public interface StatieService {

	 List<Statie> findByParcareId(int parcareId);
	
	public void saveStatie(Statie statie);
	
	public Statie findById(int id);
}
