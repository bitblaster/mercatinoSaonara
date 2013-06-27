package it.mozzicato.mercatino.persistent;

public class ClasseScolastica implements java.io.Serializable {
	private Integer idClasse;
	private String descrizione;

	public Integer getIdClasse() {
		return idClasse;
	}

	public void setIdClasse(Integer idClasse) {
		this.idClasse = idClasse;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
}
