package com.tesla.parkingapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tesla.parkingapp.model.TipIncarcare;
import com.tesla.parkingapp.repository.TipIncarcareRepository;

@Service("tipIncarcareService")
public class TipIncarcareServiceImpl implements TipIncarcareService{

	 @Autowired
	 private TipIncarcareRepository tipIncarcareRepository;
	
	@Override
	public TipIncarcare findById(int id) {
		return tipIncarcareRepository.findById(id);
	}

}
