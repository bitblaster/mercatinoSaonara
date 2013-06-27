package it.mozzicato.mercatino.client.services;

import com.google.gwt.user.client.rpc.*;

@RemoteServiceRelativePath("mercatino_service")
public interface IMercatinoServiceWeb extends RemoteService {
	boolean prenota(int idCopia);
	boolean wishList(String isbn);
	boolean modificaDatiUtente(String nome, String cognome, String telefono);
	String cambiaPassword(String vecchiaPassword, String nuovaPassword);
}
