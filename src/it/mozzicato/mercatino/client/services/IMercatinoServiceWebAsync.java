package it.mozzicato.mercatino.client.services;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface IMercatinoServiceWebAsync {

	void prenota(int idCopia, AsyncCallback<Boolean> callback);

	void wishList(String isbn, AsyncCallback<Boolean> callback);

	void modificaDatiUtente(String nome, String cognome, String telefono, AsyncCallback<Boolean> callback);

	void cambiaPassword(String vecchiaPassword, String nuovaPassword, AsyncCallback<String> callback);
}
