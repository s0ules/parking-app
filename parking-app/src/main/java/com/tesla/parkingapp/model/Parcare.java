package com.tesla.parkingapp.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "parcare")
public class Parcare {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id_parcare;
	
	@Column(name = "nr_locuri")
	private int nr_locuri;
	
	@Column(name = "adresa")
	private String adresa;
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "programare_parcare", joinColumns = @JoinColumn(name = "id_parcare"), inverseJoinColumns = @JoinColumn(name = "id_programare"))
	private Set<Programare> programari;
	
	public Parcare() {
		
	}
	
	public Parcare(int nr_locuri, String adresa) {
		super();
		this.nr_locuri = nr_locuri;
		this.adresa = adresa;
	}

	public int getId_parcare() {
		return id_parcare;
	}

	public void setId_parcare(int id_parcare) {
		this.id_parcare = id_parcare;
	}

	public int getNr_locuri() {
		return nr_locuri;
	}

	public void setNr_locuri(int nr_locuri) {
		this.nr_locuri = nr_locuri;
	}

	public String getAdresa() {
		return adresa;
	}

	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}

	public Set<Programare> getRoles() {
		return programari;
	}

	public void setRoles(Set<Programare> programari) {
		this.programari = programari;
	}

	@Override
	public String toString() {
		return "Parcare [nr_locuri=" + nr_locuri + ", adresa=" + adresa + "]";
	}
	
	
	
}
