package it.mozzicato.mercatino.persistent;

import it.mozzicato.mercatino.persistent.enums.CondizioneCopia;

import java.io.Serializable;
import java.util.*;

public class Copia implements Comparable<Copia>, Serializable {
	private int idCopia;
	private Libro libro;
	private CondizioneCopia condizione;
	private Date dataInserimento;
	private Date dataVendita;
	private Date dataRimozione;
	private String note;
	private Utente utente=new Utente();
	private List<Prenotazione> prenotazioni = new ArrayList<Prenotazione>();
	private int numPrenotazioni;
	private boolean prenotataDaUser;

	public int getIdCopia() {
		return idCopia;
	}

	public void setIdCopia(int idCopia) {
		this.idCopia = idCopia;
	}

	public Libro getLibro() {
		return libro;
	}

	public void setLibro(Libro libro) {
		this.libro = libro;
	}

	public CondizioneCopia getCondizione() {
		return condizione;
	}

	public void setCondizione(CondizioneCopia condizione) {
		this.condizione = condizione;
	}

	public Date getDataInserimento() {
		return dataInserimento;
	}

	public void setDataInserimento(Date dataInserimento) {
		this.dataInserimento = dataInserimento;
	}

	public Date getDataVendita() {
		return dataVendita;
	}

	public void setDataVendita(Date dataVendita) {
		this.dataVendita = dataVendita;
	}

	public Date getDataRimozione() {
		return dataRimozione;
	}

	public void setDataRimozione(Date dataRimozione) {
		this.dataRimozione = dataRimozione;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Utente getUtente() {
		return utente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}

	public List<Prenotazione> getPrenotazioni() {
		return prenotazioni;
	}

	public void setPrenotazioni(List<Prenotazione> prenotazioni) {
		this.prenotazioni = prenotazioni;
	}

	@Override
	public int compareTo(Copia o) {
		if(o == null)
			return 1;
		
		return idCopia - o.idCopia;
	}

	public int getNumPrenotazioni() {
		return numPrenotazioni;
	}
	
	public void setNumPrenotazioni(int numPrenotazioni) {
		this.numPrenotazioni=numPrenotazioni;
	}

	public boolean isPrenotataDaUser() {
		return prenotataDaUser;
	}
	
	public void setPrenotataDaUser(boolean prenotataDaUser) {
		this.prenotataDaUser = prenotataDaUser;
	}
}
