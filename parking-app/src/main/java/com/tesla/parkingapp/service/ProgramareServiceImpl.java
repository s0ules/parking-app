package com.tesla.parkingapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tesla.parkingapp.model.Programare;
import com.tesla.parkingapp.repository.ProgramareRepository;

@Service("programareSerivce")
public class ProgramareServiceImpl implements ProgramareService{

	@Autowired
	ProgramareRepository programareRepository;
	
	@Override
	public List<Programare> findByStatie_StatieId(int id_statie) {
		return programareRepository.findByStatie_StatieId(id_statie);
	}

	@Override
	public void saveProgramare(Programare programare) {
		programareRepository.save(programare);
	}

}
