package com.tesla.parkingapp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import java.util.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "programare")
public class Programare {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id_programare;
	
	@Column(name = "ora_inceput")
	@Temporal(TemporalType.TIMESTAMP)
	private Date ora_inceput;
	
	@Column(name = "ora_sfarsit")
	@Temporal(TemporalType.TIMESTAMP)
	private Date ora_sfarsit;
	
	@ManyToOne
    @JoinColumn(name = "statie_id")
    private Statie statie;
	
	public Programare() {
		
	}
	
	public Programare(Date ora_inceput,Date ora_sfarsit) {
		super();
		this.ora_inceput = ora_inceput;
		this.ora_sfarsit = ora_sfarsit;
	}
	
	public int getId_programare() {
		return id_programare;
	}

	public void setId_programare(int id_programare) {
		this.id_programare = id_programare;
	}

	public Date getOra_inceput() {
		return ora_inceput;
	}

	public void setOra_inceput(Date ora_inceput) {
		this.ora_inceput = ora_inceput;
	}

	public Date getOra_sfarsit() {
		return ora_sfarsit;
	}

	public void setOra_sfarsit(Date ora_sfarsit) {
		this.ora_sfarsit = ora_sfarsit;
	}

	public Statie getStatie() {
		return statie;
	}

	public void setStatie(Statie statie) {
		this.statie = statie;
	}
	
	
}
