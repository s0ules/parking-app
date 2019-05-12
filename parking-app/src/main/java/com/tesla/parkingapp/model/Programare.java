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

import com.tesla.parkingapp.service.StatieService;
import com.tesla.parkingapp.service.StatieServiceImpl;
import com.tesla.parkingapp.service.TipIncarcareService;
import com.tesla.parkingapp.service.TipIncarcareServiceImpl;

import java.util.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "programare")
public class Programare {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id_programare;
	
	@ManyToOne
    @JoinColumn(name = "tip_id")
	TipIncarcare tip_incarcare;
	
	@Column(name = "ora_inceput")
	//@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime ora_inceput;
	
	@Column(name = "ora_sfarsit")
	//@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime ora_sfarsit;
	
	@ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
	
	@ManyToOne
    @JoinColumn(name = "statie_id")
    private Statie statie;
	
	private int id_statie;
	private int id_user;
	
	public Programare() {
		
	}
	
	public Programare(LocalDateTime ora_inceput,LocalDateTime ora_sfarsit) {
		super();
		this.ora_inceput = ora_inceput;
		this.ora_sfarsit = ora_sfarsit;
	}
	
	public Programare(TipIncarcare tip_incarcare, LocalDateTime ora_inceput, LocalDateTime ora_sfarsit, Statie statie, User user) {
		
		this.tip_incarcare = tip_incarcare;
		this.ora_inceput = ora_inceput;
		this.ora_sfarsit = ora_sfarsit;
		this.statie = statie;
		this.user = user;
	}
	
	public Programare(Boolean fast, LocalDateTime ora_inceput, LocalDateTime ora_sfarsit, int statie, int user) {
		TipIncarcareService ts = new TipIncarcareServiceImpl();
		int id = 1;
		if (fast)
			id = 2;
		this.tip_incarcare = ts.findById(id);
		this.ora_inceput = ora_inceput;
		this.ora_sfarsit = ora_sfarsit;
		this.id_statie = statie;
		this.id_user = user;
	}

	public int getId_programare() {
		return id_programare;
	}

	public void setId_programare(int id_programare) {
		this.id_programare = id_programare;
	}
	
	public TipIncarcare getTip_incarcare() {
		return tip_incarcare;
	}
	
	public void setTip_incarcare(TipIncarcare tip_incarcare) {
		this.tip_incarcare = tip_incarcare;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public LocalDateTime getOra_inceput() {
		return ora_inceput;
	}

	public void setOra_inceput(LocalDateTime ora_inceput) {
		this.ora_inceput = ora_inceput;
	}

	public LocalDateTime getOra_sfarsit() {
		return ora_sfarsit;
	}

	public void setOra_sfarsit(LocalDateTime ora_sfarsit) {
		this.ora_sfarsit = ora_sfarsit;
	}

	public Statie getStatie() {
		return statie;
	}

	public void setStatie(Statie statie) {
		this.statie = statie;
	}

	public int getId_statie() {
		return id_statie;
	}

	public void setId_statie(int id_statie) {
		this.id_statie = id_statie;
	}

	public int getId_user() {
		return id_user;
	}

	public void setId_user(int id_user) {
		this.id_user = id_user;
	}
	
	
	
	
}
