package it.mozzicato.mercatino.server.controllers;

import javax.servlet.http.Cookie;

import it.infracom.jwolf.JWApplication;
import it.infracom.jwolf.authorization.User;
import it.infracom.jwolfgwt.server.service.WebLoginServiceImpl;
import it.mozzicato.mercatino.client.services.IMercatinoWebLoginService;
import it.mozzicato.mercatino.persistent.Utente;
import it.mozzicato.mercatino.server.MercatinoConfiguration;
import it.mozzicato.mercatino.server.services.MercatinoBusinessService;

public class MercatinoWebLoginServiceImpl extends WebLoginServiceImpl implements IMercatinoWebLoginService {
	@Override
	public User getLoggedUser() {
		boolean newUser = true;
		Cookie[] cookies = perThreadRequest.get().getCookies();
		if(cookies != null) {
			for (Cookie cookie : cookies) {
				if(cookie.getName().equals("welcomeDone")) {
					newUser = false;
					break;
				}
			}
		}
		if(newUser) {
			MercatinoBusinessService service = JWApplication.getService(MercatinoConfiguration.SERVIZIO_BUSINESS);
			service.addVisitCount();	
		}
		
		return super.getLoggedUser();
	}
	
	@Override
	public boolean registerUser(String nome, String cognome, String email, String password, String telefono) {
		MercatinoBusinessService service = JWApplication.getService(MercatinoConfiguration.SERVIZIO_BUSINESS);
		Utente utente = service.registerUser(nome, cognome, email, password, telefono);
		if(utente != null) {
			login(utente.getUserId(), utente.getPassword());
			return true;
		}
		return false;
	}
}
