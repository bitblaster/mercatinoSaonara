package it.mozzicato.mercatino.persistent;

import java.io.Serializable;

public class Disciplina implements Serializable {
	private Integer idDisciplina;
	private String descrizione;

	public Integer getIdDisciplina() {
		return idDisciplina;
	}

	public void setIdDisciplina(Integer idDisciplina) {
		this.idDisciplina = idDisciplina;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
}