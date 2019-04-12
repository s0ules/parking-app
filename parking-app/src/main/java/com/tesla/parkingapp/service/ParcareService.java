package com.tesla.parkingapp.service;

import java.util.List;

import com.tesla.parkingapp.model.Parcare;

public interface ParcareService {
	
	public List<Parcare> findAll();
	
	public void saveParcare(Parcare parcare);
}
