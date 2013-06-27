package it.mozzicato.mercatino.persistent;

import java.util.Date;

public class WishList implements java.io.Serializable {

	private long idWishList;
	private Libro libro = new Libro();
	private Utente utente = new Utente();
	private Date dataAggiunta;
	private Date dataRimozione;

	public long getIdWishList() {
		return idWishList;
	}

	public void setIdWishList(long idWishList) {
		this.idWishList = idWishList;
	}

	public Libro getLibro() {
		return libro;
	}

	public void setLibro(Libro libro) {
		this.libro = libro;
	}

	public Utente getUtente() {
		return utente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}

	public Date getDataAggiunta() {
		return dataAggiunta;
	}

	public void setDataAggiunta(Date dataAggiunta) {
		this.dataAggiunta = dataAggiunta;
	}

	public Date getDataRimozione() {
		return dataRimozione;
	}

	public void setDataRimozione(Date dataRimozione) {
		this.dataRimozione = dataRimozione;
	}
}
