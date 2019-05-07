package com.tesla.parkingapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tesla.parkingapp.model.Statie;

@Repository("statieRepository")
public interface StatieRepository extends JpaRepository<Statie, Integer>{

	List<Statie> findByParcare_ParcareId(int id_parcare);
	
}
