package com.tesla.parkingapp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "parcari")
public class Parcare {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_parcare")
	private int id_parcare;
	@Column(name = "nr_locuri")
	private int nr_locuri;
	private Adresa adresa;
	
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
	public Adresa getAdresa() {
		return adresa;
	}
	public void setAdresa(Adresa adresa) {
		this.adresa = adresa;
	}
	
	@Override
	public String toString() {
		return "Parcare [id_parcare=" + id_parcare + ", nr_locuri=" + nr_locuri + ", adresa=" + adresa + "]";
	}
	
	
	
}
