package com.tesla.parkingapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tesla.parkingapp.model.TipIncarcare;

@Repository("tipIncarcareRepository")
public interface TipIncarcareRepository extends JpaRepository<TipIncarcare, Integer>{
	
	TipIncarcare findById(int id);
}
