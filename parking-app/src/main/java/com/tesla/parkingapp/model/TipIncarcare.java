package com.tesla.parkingapp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tip_incarcare")
public class TipIncarcare {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int tip_id;
	
	@Column(name="nume_tip")
	String nume_tip;

	public TipIncarcare() {
		
	}
	
	public TipIncarcare(int tip_id, String nume_tip) {
		super();
		this.tip_id = tip_id;
		this.nume_tip = nume_tip;
	}

	public int getTip_id() {
		return tip_id;
	}

	public void setTip_id(int tip_id) {
		this.tip_id = tip_id;
	}

	public String getNume_tip() {
		return nume_tip;
	}

	public void setNume_tip(String nume_tip) {
		this.nume_tip = nume_tip;
	}
	
	
}
