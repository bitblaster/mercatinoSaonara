package it.mozzicato.mercatino.persistent;

import java.io.Serializable;
import java.util.*;

public class Libro implements Comparable<Libro>, Serializable {
	private String isbn;
	private String titolo;
	private String descrizione;
	private String autore;
	private String editore;
	private Integer anno;
	private Disciplina disciplina;
	private double prezzo;
	private List<ClasseScolastica> classi = new ArrayList<ClasseScolastica>();
	private List<Copia> copie = new ArrayList<Copia>();
	private List<WishList> wishLists = new ArrayList<WishList>();

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getAutore() {
		return autore;
	}

	public void setAutore(String autore) {
		this.autore = autore;
	}

	public String getEditore() {
		return editore;
	}

	public void setEditore(String editore) {
		this.editore = editore;
	}

	public Integer getAnno() {
		return anno;
	}

	public void setAnno(Integer anno) {
		this.anno = anno;
	}

	public List<Copia> getCopie() {
		return copie;
	}

	public void setCopie(List<Copia> copie) {
		this.copie = copie;
	}

	public Disciplina getDisciplina() {
		return disciplina;
	}

	public void setDisciplina(Disciplina disciplina) {
		this.disciplina = disciplina;
	}

	public double getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(double prezzo) {
		this.prezzo = prezzo;
	}

	public List<ClasseScolastica> getClassi() {
		return classi;
	}
	
	public void setClassi(List<ClasseScolastica> classi) {
		this.classi = classi;
	}
	
	public List<WishList> getWishLists() {
		return wishLists;
	}

	public void setWishLists(List<WishList> wishLists) {
		this.wishLists = wishLists;
	}

	@Override
	public int compareTo(Libro o) {
		if (o == null)
			return 1;

		return isbn.compareTo(o.isbn);
	}
}
