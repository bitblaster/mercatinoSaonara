package it.mozzicato.mercatino.server.controllers;

import java.io.Serializable;
import java.util.Map;

import it.infracom.jwolf.JWApplication;
import it.infracom.jwolf.exception.ServiceException;
import it.infracom.jwolf.service.ICrudService;
import it.infracom.jwolfgwt.server.service.WebCrudServiceImpl;
import it.mozzicato.mercatino.persistent.*;
import it.mozzicato.mercatino.server.MercatinoConfiguration;

public class MercatinoWebCrudServiceImpl extends WebCrudServiceImpl {
	public MercatinoWebCrudServiceImpl() {
		setAuthenticationChecked(false);
	}
	
	@Override
	protected <T extends Serializable> ICrudService<T> getCrudService(Class<T> crudClass, Map<String, Serializable> parameters) throws ServiceException {
		if(crudClass == Libro.class)
			return JWApplication.getCrudService(MercatinoConfiguration.SERVIZIO_CRUD_LIBRI, crudClass);
		if(crudClass == Copia.class)
			return JWApplication.getCrudService(MercatinoConfiguration.SERVIZIO_CRUD_COPIE, crudClass);
		if(crudClass == Prenotazione.class)
			return JWApplication.getCrudService(MercatinoConfiguration.SERVIZIO_CRUD_PRENOTAZIONI, crudClass);
		
		return super.getCrudService(crudClass, parameters);
	}
}
