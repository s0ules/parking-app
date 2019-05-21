package com.tesla.parkingapp.service;

import java.util.List;

import com.tesla.parkingapp.model.Parcare;

public interface ParcareService {
	
	public List<Parcare> findAll();
	
	public Parcare findById(int id);
	
	public List<Parcare> findByLatitudineAndLongitudine(double latitudine, double longitudine);
	
	public List<Parcare> findByUser_UserId(int user_id);
	
	public void saveParcare(Parcare parcare);
	
	public void updateParcare(Parcare parcare);
	
	public void deleteParcare(int id);
}
