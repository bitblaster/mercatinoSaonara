package it.mozzicato.mercatino.client.services;

import it.infracom.jwolfgwt.client.service.IWebLoginService;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("jwolfgwt_login")
public interface IMercatinoWebLoginService extends IWebLoginService {
	boolean registerUser(String nome, String cognome, String email, String password, String telefono);
}
