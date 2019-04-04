package com.tesla.parkingapp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "adresa")
public class Adresa {
	
	@Column(name = "id_parcare")
	private int id; 
	
	@Column(name = "Judet")
	private String Judet;
	
	@Column(name = "Oras")
	private String Oras;
	
	@Column(name = "Sector")
	private String Sector;
	
	@Column(name = "Adresa")
	private String Adresa;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getJudet() {
		return Judet;
	}

	public void setJudet(String judet) {
		Judet = judet;
	}

	public String getOras() {
		return Oras;
	}

	public void setOras(String oras) {
		Oras = oras;
	}

	public String getSector() {
		return Sector;
	}

	public void setSector(String sector) {
		Sector = sector;
	}

	public String getAdresa() {
		return Adresa;
	}

	public void setAdresa(String adresa) {
		Adresa = adresa;
	}

	@Override
	public String toString() {
		return "Adresa [id=" + id + ", Judet=" + Judet + ", Oras=" + Oras + ", Sector=" + Sector + ", Adresa=" + Adresa
				+ "]";
	}
	
	
}
