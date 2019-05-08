package com.tesla.parkingapp.service;

import java.util.List;

import com.tesla.parkingapp.model.Programare;

public interface ProgramareService {
	public List<Programare> findByStatie_StatieId(int id_statie);
}
