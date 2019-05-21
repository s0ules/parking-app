package com.tesla.parkingapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tesla.parkingapp.model.Programare;

@Repository("programareRepository")
public interface ProgramareRepository extends JpaRepository<Programare, Integer>{
	
	List<Programare> findByStatie_StatieId(int statie_id);
	
	List<Programare> findByUser_Id(int user_id);
	
}
