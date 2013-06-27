package it.mozzicato.mercatino.server.services;

import it.infracom.jwolf.extensions.hibernate.service.HibernateCrudService;
import it.mozzicato.mercatino.persistent.Prenotazione;

import java.util.Date;

import org.hibernate.*;
import org.hibernate.criterion.Restrictions;

public class PrenotazioneCrudService extends HibernateCrudService {
	
	ThreadLocal<Prenotazione> prenotazioneUpdate = new ThreadLocal<Prenotazione>();
	@Override
	public void update(Object entity) {
		prenotazioneUpdate.set(null);
		super.update(entity);
	
		if(prenotazioneUpdate.get() != null)
			notifyPrenotazioni(prenotazioneUpdate.get());
	}
	
	@Override
	protected void internalUpdate(Object entity) {
		Session session=manager.getSession();
		Criteria criteria = session.createCriteria(Prenotazione.class);
		criteria.add(Restrictions.eq("idPrenotazione", ((Prenotazione)entity).getIdPrenotazione()));
		Prenotazione prenotazione = (Prenotazione) criteria.uniqueResult();
		prenotazione.setDataRimozione(new Date());
		session.update(prenotazione);
		prenotazioneUpdate.set(prenotazione);
	}
	
	private void notifyPrenotazioni(Prenotazione prenotazione) {
		if(prenotazione == null)
			return;
		
//		MailService mailService = JWApplication.getService(MercatinoConfiguration.SERVIZIO_MAIL);
//		for (Prenotazione prenot : prenotazione.getPrenotazioni()) {
//			try {
//				mailService.sendEmail("Conferma prenotazione", 
//					"Gentile " + prenot.getUtente().getFirstName() + ",<br/>" +
//					"ti informiamo che l'utente " + prenotazione.getUtente().getFullName() + " ha appena rimosso dalla vendita la copia che avevi prenotato del seguente libro:<br/>" +
//					"<b>ISBN:</b>&nbsp;" + prenotazione.getLibro().getIsbn() + "<br/>" +
//					"<b>Titolo:</b>&nbsp;" + prenotazione.getLibro().getTitolo() + "<br/>" +
//					"<b>Descrizione:</b>&nbsp;" + prenotazione.getLibro().getDescrizione() + "<br/>" +
//					"<b>Autore:</b>&nbsp;" + prenotazione.getLibro().getAutore() + "<br/>" +
//					"<b>Editore:</b>&nbsp;" + prenotazione.getLibro().getEditore() + "<br/>" +
//					"<b>Prezzo:</b>&nbsp;" + prenotazione.getLibro().getPrezzo() + "<br/>" +
//					"A presto!<br/><br/>" +
//					"<b>Staff Mercatino dei libri usati</b><br/>" +
//					"Comitato genitori Saonara", 
//					prenot.getUtente().getFullName() + "<" + prenot.getUtente().getUserId() + ">");
//			}
//			catch (Exception e) {
//				logger.error("Errore durante l'invio dell'email di rimozione prenotazione:\n" + StringUtils.stackTraceToString(e));
//			}
//		}
	}
}
