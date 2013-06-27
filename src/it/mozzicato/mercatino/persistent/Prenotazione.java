package it.mozzicato.mercatino.persistent;

import java.util.Date;

public class Prenotazione implements java.io.Serializable {

	private long idPrenotazione;
	private Copia copia=new Copia();
	private Utente utente=new Utente();
	private Date dataPrenotazione;
	private Date dataRimozione;

	public long getIdPrenotazione() {
		return idPrenotazione;
	}
	
	public void setIdPrenotazione(long idPrenotazione) {
		this.idPrenotazione = idPrenotazione;
	}
	
	public Copia getCopia() {
		return copia;
	}

	public void setCopia(Copia copia) {
		this.copia = copia;
	}

	public Utente getUtente() {
		return utente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}

	public Date getDataPrenotazione() {
		return dataPrenotazione;
	}

	public void setDataPrenotazione(Date dataPrenotazione) {
		this.dataPrenotazione = dataPrenotazione;
	}

	public Date getDataRimozione() {
		return dataRimozione;
	}

	public void setDataRimozione(Date dataRimozione) {
		this.dataRimozione = dataRimozione;
	}

}
