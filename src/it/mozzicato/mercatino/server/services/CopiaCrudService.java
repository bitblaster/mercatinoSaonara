package it.mozzicato.mercatino.server.services;

import it.infracom.jwolf.JWApplication;
import it.infracom.jwolf.authorization.User;
import it.infracom.jwolf.extensions.hibernate.service.HibernateCrudService;
import it.infracom.jwolf.service.TransactionBlock;
import it.infracom.jwolf.utils.StringUtils;
import it.infracom.jwolf.utils.criteria.*;
import it.mozzicato.mercatino.MailService;
import it.mozzicato.mercatino.persistent.*;
import it.mozzicato.mercatino.server.MercatinoConfiguration;

import java.io.Serializable;
import java.util.*;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.hibernate.*;
import org.hibernate.criterion.Restrictions;

public class CopiaCrudService extends HibernateCrudService {
	
	@Override
	protected List<Object> internalRead(List<Criterion> criterions, Pagination pagination, Sorting<Object> sorting) {
		List<Object> ret =super.internalRead(criterions, pagination, sorting);
		
		if(parameters.get().get("LEGGI_PRENOTAZIONI") != null) {
			User user = JWApplication.getWorkSession().getUser();
			Session s = manager.getSession();
//			Criteria critPrenot = s.createCriteria(Prenotazione.class);
//			critPrenot.add(Restrictions.eq("utente.userId", user.getUserId()));
//			critPrenot.addOrder(Order.asc("copia.idCopia"));
//			List<Prenotazione> listaPrenotazioni = critPrenot.list();
//			ArrayList<Copia> copiePrenotate = new ArrayList<Copia>();
//			
//			for (Prenotazione prenotazione : listaPrenotazioni) {
//				copiePrenotate.add(prenotazione.getCopia());
//			}
			
//			for (Copia copia : (List<Copia>) (List) ret) {
//				int indice = Collections.binarySearch(copiePrenotate, copia);
//				if(indice >= 0) {
//					copia.setPrenotazioni(new ArrayList<Prenotazione>(1));
//					copia.getPrenotazioni().add(listaPrenotazioni.get(indice));
//				}
//			}
			
			for (Copia copia : (List<Copia>) (List) ret) {
				Criteria critPrenot = s.createCriteria(Prenotazione.class);
				critPrenot.add(Restrictions.eq("copia.idCopia", copia.getIdCopia()));
				critPrenot.add(Restrictions.isNull("dataRimozione"));
				List<Prenotazione> listaPrenotazioni = critPrenot.list();
				copia.setNumPrenotazioni(listaPrenotazioni.size());
				if(user != null) {
					for (Prenotazione prenotazione : listaPrenotazioni) {
						if(prenotazione.getUtente().getUserId().equals(user.getUserId())) {
							copia.setPrenotataDaUser(true);
							break;
						}
					}
				}
//				int indice = Collections.binarySearch(copiePrenotate, copia);
//				if(indice >= 0) {
//					copia.setPrenotazioni(new ArrayList<Prenotazione>(1));
//					copia.getPrenotazioni().add(listaPrenotazioni.get(indice));
//				}
			}

		}
		
		return ret;
	}
	
	@Override
	public Serializable create(final Object entity) {
		Serializable ret = super.create(entity);
		
		executeInTransaction(new TransactionBlock<Void>() {
			@Override
			public Void run() throws Throwable {
				notifyWishLists((Copia) entity);
				return null;
			}
		});
		
		return ret;
	}
	
	protected void notifyWishLists(Copia entity) throws AddressException, MessagingException {
		Session session = manager.getSession();
		Criteria crit = session.createCriteria(WishList.class);
		crit.add(Restrictions.eq("libro.isbn", entity.getLibro().getIsbn()));
		crit.setFetchMode("utente", FetchMode.JOIN);
		crit.setFetchMode("libro", FetchMode.JOIN);
		List<WishList> wishLists = crit.list();
		
		MailService mailService = JWApplication.getService(MercatinoConfiguration.SERVIZIO_MAIL);
		for (WishList wishList : wishLists) {
			mailService.sendEmail("Conferma prenotazione", 
				"Gentile " + wishList.getUtente().getFirstName() + ",<br/>" +
				"ti informiamo che un utente ha appena messo in vendita una copia del seguente libro che stai cercando:<br/>" +
				"<b>ISBN:</b>&nbsp;" + wishList.getLibro().getIsbn() + "<br/>" +
				"<b>Titolo:</b>&nbsp;" + wishList.getLibro().getTitolo() + "<br/>" +
				"<b>Descrizione:</b>&nbsp;" + wishList.getLibro().getDescrizione() + "<br/>" +
				"<b>Autore:</b>&nbsp;" + wishList.getLibro().getAutore() + "<br/>" +
				"<b>Editore:</b>&nbsp;" + wishList.getLibro().getEditore() + "<br/>" +
				"<b>Prezzo:</b>&nbsp;" + wishList.getLibro().getPrezzo() + "<br/>" +
				"Puoi prenotare la copia andando sul nostro sito, cercando direttamente l'ISBN del libro e cliccando su Prenota dopo aver selezionato la copia scelta.<br/>" +
				"A presto!<br/><br/>" +
				"<b>Staff Mercatino dei libri usati</b><br/>" +
				"Comitato genitori Saonara", 
				wishList.getUtente().getFullName() + "<" + wishList.getUtente().getUserId() + ">");
		}		
	}

	@Override
	protected Serializable internalCreate(Object entity) {
		Copia copia = (Copia) entity;
		copia.getUtente().setUserId(JWApplication.getWorkSession().getUser().getUserId());
		return super.internalCreate(entity);
	}
	
	ThreadLocal<Copia> copiaUpdate = new ThreadLocal<Copia>();
	@Override
	public void update(Object entity) {
		copiaUpdate.set(null);
		super.update(entity);
	
		if(copiaUpdate.get() != null)
			notifyPrenotazioni(copiaUpdate.get());
	}
	
	@Override
	protected void internalUpdate(Object entity) {
		Session session=manager.getSession();
		Copia updatedCopia = ((Copia)entity);
		if(updatedCopia != null && (updatedCopia.getDataRimozione() != null || updatedCopia.getDataVendita() != null)) {
			Criteria criteria = session.createCriteria(Copia.class);
			criteria.add(Restrictions.eq("idCopia", ((Copia)entity).getIdCopia()));
//			criteria.setFetchMode("libro", FetchMode.JOIN);
//			criteria.setFetchMode("utente", FetchMode.JOIN);
//			criteria.setFetchMode("prenotazioni", FetchMode.JOIN);
//			criteria.setFetchMode("prenotazioni.utente", FetchMode.JOIN);
			Copia copia = (Copia) criteria.uniqueResult();
			
			if(copia != null) {
				copia.setDataVendita(updatedCopia.getDataVendita());
				copia.setDataRimozione(updatedCopia.getDataRimozione());
				copia.setPrenotazioni(new ArrayList<Prenotazione>());
				
				Criteria critPrenot = session.createCriteria(Prenotazione.class);
				critPrenot.add(Restrictions.eq("copia.idCopia", copia.getIdCopia()));
				critPrenot.add(Restrictions.isNull("dataRimozione"));
				List<Prenotazione> listaPrenotazioni = critPrenot.list();
				for (Prenotazione prenotazione : listaPrenotazioni) {
					prenotazione.setDataRimozione(new Date());
					session.update(prenotazione);
					copia.getPrenotazioni().add(prenotazione);
				}
			}
			updatedCopia = copia;
		}
		super.internalUpdate(updatedCopia);
		copiaUpdate.set(updatedCopia);
	}
	
	private void notifyPrenotazioni(Copia copia) {
		if(copia == null)
			return;
		
		MailService mailService = JWApplication.getService(MercatinoConfiguration.SERVIZIO_MAIL);
		for (Prenotazione prenot : copia.getPrenotazioni()) {
			try {
				mailService.sendEmail("Conferma prenotazione", 
					"Gentile " + prenot.getUtente().getFirstName() + ",<br/>" +
					"ti informiamo che l'utente " + copia.getUtente().getFullName() + " ha appena rimosso dalla vendita la copia che avevi prenotato del seguente libro:<br/>" +
					"<b>ISBN:</b>&nbsp;" + copia.getLibro().getIsbn() + "<br/>" +
					"<b>Titolo:</b>&nbsp;" + copia.getLibro().getTitolo() + "<br/>" +
					"<b>Descrizione:</b>&nbsp;" + copia.getLibro().getDescrizione() + "<br/>" +
					"<b>Autore:</b>&nbsp;" + copia.getLibro().getAutore() + "<br/>" +
					"<b>Editore:</b>&nbsp;" + copia.getLibro().getEditore() + "<br/>" +
					"<b>Prezzo:</b>&nbsp;" + copia.getLibro().getPrezzo() + "<br/>" +
					"A presto!<br/><br/>" +
					"<b>Staff Mercatino dei libri usati</b><br/>" +
					"Comitato genitori Saonara", 
					prenot.getUtente().getFullName() + "<" + prenot.getUtente().getUserId() + ">");
			}
			catch (Exception e) {
				logger.error("Errore durante l'invio dell'email di rimozione prenotazione:\n" + StringUtils.stackTraceToString(e));
			}
		}
	}
}
