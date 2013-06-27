package it.mozzicato.mercatino.server.controllers;

import it.infracom.jwolf.JWApplication;
import it.infracom.jwolfgwt.server.service.LoggedOnlyServiceServlet;
import it.mozzicato.mercatino.client.services.IMercatinoServiceWeb;
import it.mozzicato.mercatino.server.MercatinoConfiguration;
import it.mozzicato.mercatino.server.services.MercatinoBusinessService;

public class MercatinoServiceWebImpl extends LoggedOnlyServiceServlet implements IMercatinoServiceWeb {

	@Override
	public boolean prenota(int idCopia) {
		MercatinoBusinessService service = JWApplication.getService(MercatinoConfiguration.SERVIZIO_BUSINESS);
		service.prenota(idCopia);
		
		return true;
	}

	@Override
	public boolean wishList(String isbn) {
		MercatinoBusinessService service = JWApplication.getService(MercatinoConfiguration.SERVIZIO_BUSINESS);
		service.wishList(isbn);
		
		return true;
	}

	@Override
	public boolean modificaDatiUtente(String nome, String cognome, String telefono) {
		MercatinoBusinessService service = JWApplication.getService(MercatinoConfiguration.SERVIZIO_BUSINESS);
		service.modificaDatiUtente(nome, cognome, telefono);

		return true;
	}

	@Override
	public String cambiaPassword(String vecchiaPassword, String nuovaPassword) {
		MercatinoBusinessService service = JWApplication.getService(MercatinoConfiguration.SERVIZIO_BUSINESS);
		return service.cambiaPassword(vecchiaPassword, nuovaPassword);
	}
}
