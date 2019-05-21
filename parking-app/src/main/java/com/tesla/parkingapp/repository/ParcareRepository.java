package com.tesla.parkingapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tesla.parkingapp.model.Parcare;

@Repository("parcareRepository")
public interface ParcareRepository extends JpaRepository<Parcare, Integer>{
	
	 List<Parcare> findAll();
	
	 List<Parcare> findByLatitudineAndLongitudine(double latitudine, double longitudine);
	 
	 List<Parcare> findByUser_Id(int user_id);
	 
	 Parcare findById(int parcare_id);
	 
}
