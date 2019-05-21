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

	@Override
	public Parcare findById(int id) {
		return parcareRepository.findById(id);
	}

	@Override
	public List<Parcare> findByLatitudineAndLongitudine(double latitudine, double longitudine) {
		return parcareRepository.findByLatitudineAndLongitudine(latitudine, longitudine);
	}

	@Override
	public List<Parcare> findByUser_UserId(int user_id) {
		return parcareRepository.findByUser_Id(user_id);
	}
}
