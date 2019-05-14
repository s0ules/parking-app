package com.tesla.parkingapp.service;

import java.util.List;

import com.tesla.parkingapp.model.Programare;

public interface ProgramareService {
	public List<Programare> findByStatie_StatieId(int statie_id);
	
	public List<Programare> findByUser_UserId(int user_id);
	
	public void saveProgramare(Programare programare);
}
