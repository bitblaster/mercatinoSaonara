package it.mozzicato.mercatino.client.services;

import it.infracom.jwolfgwt.client.service.IWebLoginServiceAsync;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface IMercatinoWebLoginServiceAsync extends IWebLoginServiceAsync {
	void registerUser(String nome, String cognome, String email, String password, String telefono, AsyncCallback<Boolean> callback);
}
