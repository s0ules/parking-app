package com.tesla.parkingapp.model;

import java.time.LocalTime;
import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
	
	@Column(name = "ora_deschidere")
	private LocalTime oraDeschidere;
	
	@Column(name = "ora_inchidere")
	private LocalTime oraInchidere;
	
/*	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "programare_parcare", joinColumns = @JoinColumn(name = "id_parcare"), inverseJoinColumns = @JoinColumn(name = "id_programare"))
	private Set<Programare> programari;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "parcare", cascade = CascadeType.ALL)
	private Set<Statie> statii;
	//private List<Statie> statii = new ArrayList<>();
	*/
	public Parcare() {
		
	}
	
	public Parcare(int parcareId, int nr_locuri, LocalTime oraDeschidere, LocalTime oraInchidere, String adresa, Boolean status, Double lat, Double lng) {
		super();
		this.parcareId = parcareId;
		this.nr_locuri = nr_locuri;
		this.oraDeschidere = oraDeschidere;
		this.oraInchidere = oraInchidere;
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
	
	public LocalTime getOraDeschidere() {
		return oraDeschidere;
	}

	public void setOraDeschidere(LocalTime oraDeschidere) {
		this.oraDeschidere = oraDeschidere;
	}

	public LocalTime getOraInchidere() {
		return oraInchidere;
	}

	public void setOraInchidere(LocalTime oraInchidere) {
		this.oraInchidere = oraInchidere;
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
