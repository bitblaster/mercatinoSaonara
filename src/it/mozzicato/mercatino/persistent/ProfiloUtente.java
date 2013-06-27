package it.mozzicato.mercatino.persistent;

import java.io.Serializable;

public class ProfiloUtente implements Serializable {
	private String userId;
	private String gruppo;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getGruppo() {
		return gruppo;
	}

	public void setGruppo(String gruppo) {
		this.gruppo = gruppo;
	}

}
