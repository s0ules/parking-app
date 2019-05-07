package com.tesla.parkingapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tesla.parkingapp.model.Statie;
import com.tesla.parkingapp.repository.StatieRepository;

@Service("statieService")
public class StatieServiceImpl implements StatieService{

	@Autowired
	 private StatieRepository statieRepository;
	
	@Override
	public void saveStatie(Statie statie) {
		statieRepository.save(statie);
	}

	@Override
	public List<Statie> findByParcareId(int parcareId) {
		return statieRepository.findByParcare_ParcareId(parcareId);
	}

}
