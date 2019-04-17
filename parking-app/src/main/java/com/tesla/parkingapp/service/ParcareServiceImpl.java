package com.tesla.parkingapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tesla.parkingapp.model.Parcare;
import com.tesla.parkingapp.repository.ParcareRepository;

@Service("parcareService")
public class ParcareServiceImpl implements ParcareService{

	@Autowired
	 private ParcareRepository parcareRepository;
	
	@Override
	public List<Parcare> findAll() {
		return parcareRepository.findAll();
	}

	@Override
	public void saveParcare(Parcare parcare) {
		parcareRepository.save(parcare);
	}

	@Override
	public void updateParcare(Parcare parcare) {
		parcareRepository.save(parcare);		
	}

	@Override
	public void deleteParcare(int id) {
		parcareRepository.deleteById(id);
	}
}
