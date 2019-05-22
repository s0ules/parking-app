package com.tesla.parkingapp.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "statie_incarcare")
public class Statie {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int statieId;
	
	@ManyToOne
    @JoinColumn(name = "parcare_id")
    private Parcare parcare;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "statie", cascade = CascadeType.ALL)
	private List<Programare> programari = new ArrayList<>();
	
	public Statie() {
		
	}
	
	public Statie(Parcare p) {
		parcare = p;
	}
	
	public int getStatieId() {
		return statieId;
	}
	
	public void setStatieId(int id_statie) {
		this.statieId = id_statie;
	}

	public Parcare getParcare() {
		return parcare;
	}

	public void setParcare(Parcare parcare) {
		this.parcare = parcare;
	}

	public List<Programare> getProgramari() {
		return programari;
	}

	public void setProgramari(List<Programare> programari) {
		this.programari = programari;
	}
	
	
}
