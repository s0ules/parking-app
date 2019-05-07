package com.tesla.parkingapp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "parcare")
public class Parcare {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int parcareId;
	
	@Column(name = "nr_locuri")
	private int nr_locuri;
	
	@Column(name = "adresa")
	private String adresa;
	
	@Column(name = "status")
	private Boolean status;
	
	@Column(name = "latitudine")
	private double latitudine;
	
	@Column(name = "longitudine")
	private double longitudine;
	
/*	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "programare_parcare", joinColumns = @JoinColumn(name = "id_parcare"), inverseJoinColumns = @JoinColumn(name = "id_programare"))
	private Set<Programare> programari;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "parcare", cascade = CascadeType.ALL)
	private Set<Statie> statii;
	//private List<Statie> statii = new ArrayList<>();
	*/
	public Parcare() {
		
	}
	
	public Parcare(int parcareId, int nr_locuri, String adresa, Boolean status, Double lat, Double lng) {
		super();
		this.parcareId = parcareId;
		this.nr_locuri = nr_locuri;
		this.adresa = adresa;
		this.status = status;
		this.latitudine = lat;
		this.longitudine = lng;
	}

	public int getParcareId() {
		return parcareId;
	}

	public void setParcareId(int parcareId) {
		this.parcareId = parcareId;
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

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public double getLatitudine() {
		return latitudine;
	}

	public void setLatitudine(double latitudine) {
		this.latitudine = latitudine;
	}

	public double getLongitudine() {
		return longitudine;
	}

	public void setLongitudine(double longitudine) {
		this.longitudine = longitudine;
	}

/*	public Set<Programare> getProgramari() {
		return programari;
	}

	public void setProgramari(Set<Programare> programari) {
		this.programari = programari;
	}
*/
	
	
	/*public List<Statie> getStatii() {
		return statii;
	}

	public void setStatii(List<Statie> statii) {
		this.statii = statii;
	}

	public Set<Statie> getStatii() {
		return statii;
	}

	public void setStatii(Set<Statie> statii) {
		this.statii = statii;
	}
	*/
	@Override
	public String toString() {
		return "Parcare [id_parcare=" + parcareId + ", nr_locuri=" + nr_locuri + ", adresa=" + adresa + ", status="
				+ status + "]";
	}

	
}
