package it.mozzicato.mercatino.server.services;

import it.infracom.jwolf.JWApplication;
import it.infracom.jwolf.exception.ServiceException;
import it.infracom.jwolf.extensions.hibernate.connection.HibernateManager;
import it.infracom.jwolf.service.*;
import it.mozzicato.mercatino.MailService;
import it.mozzicato.mercatino.persistent.*;
import it.mozzicato.mercatino.server.MercatinoConfiguration;

import java.util.*;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.hibernate.*;
import org.hibernate.criterion.Restrictions;

public class MercatinoBusinessService extends AbstractService<HibernateManager> {

	public void prenota(final int idCopia) {
		
		Copia retCopia = executeInTransaction(new TransactionBlock<Copia>() {
			@Override
			public Copia run() throws Throwable {
				Session s = manager.getSession();
				Criteria criteria = s.createCriteria(Copia.class);
				criteria.add(Restrictions.eq("idCopia", idCopia));
				criteria.setFetchMode("libro", FetchMode.JOIN);
				criteria.setFetchMode("utente", FetchMode.JOIN);
				Copia copia = (Copia) criteria.uniqueResult();
				if(copia == null)
					throw new ServiceException("Errore durante la prenotazione: copia non trovata!");
				
				Utente utente = (Utente) JWApplication.getWorkSession().getUser();
				Prenotazione prenot = new Prenotazione();
				prenot.getCopia().setIdCopia(idCopia);
				prenot.getUtente().setUserId(utente.getUserId());
				prenot.setDataPrenotazione(new Date());
				s.save(prenot);
				
				return copia;
			}
		});
		
		if(retCopia != null) {
			Utente utente = (Utente) JWApplication.getWorkSession().getUser();
			
			MailService mailService = JWApplication.getService(MercatinoConfiguration.SERVIZIO_MAIL);
			mailService.sendEmail("Conferma prenotazione", 
				"Gentile " + utente.getFirstName() + ",<br/>" +
				"ti confermiamo la prenotazione che hai effettuato sul sito del mercatino per il seguente libro:<br/>" +
				"<b>ISBN:</b>&nbsp;" + retCopia.getLibro().getIsbn() + "<br/>" +
				"<b>Titolo:</b>&nbsp;" + retCopia.getLibro().getTitolo() + "<br/>" +
				"<b>Descrizione:</b>&nbsp;" + retCopia.getLibro().getDescrizione() + "<br/>" +
				"<b>Autore:</b>&nbsp;" + retCopia.getLibro().getAutore() + "<br/>" +
				"<b>Editore:</b>&nbsp;" + retCopia.getLibro().getEditore() + "<br/>" +
				"<b>Prezzo:</b>&nbsp;" + retCopia.getLibro().getPrezzo() + "<br/><br/>" +
				"Di seguito le informazioni di contatto dell'utente che possiede la copia che hai prenotato:<br/>" +
				"<b>Nome:</b> " + retCopia.getUtente().getFullName() + "<br/>" +
				"<b>Email:</b> " + retCopia.getUtente().getUserId() + "<br/>" +
				"<b>Telefono:</b> " + retCopia.getUtente().getTelefono1() + "<br/><br/>" +
				"Ti ricordiamo che abbiamo anche inviato le tue informazioni di contatto al venditore.<br/><br/>" +
				"Grazie per aver usato il Mercatino dei libri usati!<br/>" +
				"A presto!<br/><br/>" +
				"<b>Staff Mercatino dei libri usati</b><br/>" +
				"Comitato genitori Saonara", 
				utente.getFullName() + "<" + utente.getUserId() + ">");
			
			mailService.sendEmail("Nuova prenotazione per un tuo libro!", 
				"Gentile " + retCopia.getUtente().getFirstName() + ",<br/>" +
				"un utente del Mercatino del libro usato ha effettuato una prenotazione per il seguente libro che tu hai messo in vendita:<br/>" +
				"<b>ISBN:</b>&nbsp;" + retCopia.getLibro().getIsbn() + "<br/>" +
				"<b>Titolo:</b>&nbsp;" + retCopia.getLibro().getTitolo() + "<br/>" +
				"<b>Descrizione:</b>&nbsp;" + retCopia.getLibro().getDescrizione() + "<br/>" +
				"<b>Autore:</b>&nbsp;" + retCopia.getLibro().getAutore() + "<br/>" +
				"<b>Editore:</b>&nbsp;" + retCopia.getLibro().getEditore() + "<br/>" +
				"<b>Prezzo:</b>&nbsp;" + retCopia.getLibro().getPrezzo() + "<br/><br/>" +
				"Di seguito le informazioni di contatto dell'utente che ha prenotato:<br/>" +
				"<b>Nome:</b> " + utente.getFullName() + "<br/>" +
				"<b>Email:</b> " + utente.getUserId() + "<br/>" +
				"<b>Telefono:</b> " + utente.getTelefono1() + "<br/><br/>" +
				"Ti ricordiamo che abbiamo anche inviato le tue informazioni di contatto al compratore.<br/><br/>" +
				"Grazie per aver usato il Mercatino dei libri usati!<br/>" +
				"A presto!<br/><br/>" +
				"<b>Staff Mercatino dei libri usati</b><br/>" +
				"Comitato genitori Saonara", 
				retCopia.getUtente().getFullName() + "<" + retCopia.getUtente().getUserId() + ">");
		}
	}

	public void wishList(final String isbn) {
		executeInTransaction(new TransactionBlock<Void>() {
			@Override
			public Void run() throws Throwable {
				internalWishList(isbn);
				return null;
			}
		});
	}

	protected void internalWishList(String isbn) {
		Session s = manager.getSession();
		WishList wish = new WishList();
		wish.getLibro().setIsbn(isbn);
		wish.getUtente().setUserId(JWApplication.getWorkSession().getUser().getUserId());
		wish.setDataAggiunta(new Date());
	
		s.save(wish);
	}

	public void modificaDatiUtente(final String nome, final String cognome, final String telefono) {
		executeInTransaction(new TransactionBlock<Void>() {
			@Override
			public Void run() throws Throwable {
				Session s = manager.getSession();
				Criteria criteria = s.createCriteria(Utente.class);
				criteria.add(Restrictions.eq("userId", JWApplication.getWorkSession().getUser().getUserId()));
				Utente readOne = (Utente) criteria.uniqueResult();
				if (readOne != null) {
					readOne.setFirstName(nome);
					readOne.setSurname(cognome);
					readOne.setTelefono1(telefono);
					
					s.update(readOne);
				}

				return null;
			}
		});
	}
	
	public String cambiaPassword(final String vecchiaPassword, final String nuovaPassword) {
		return executeInTransaction(new TransactionBlock<String>() {
			@Override
			public String run() throws Throwable {
				Session s = manager.getSession();
				Criteria criteria = s.createCriteria(Utente.class);
				criteria.add(Restrictions.eq("userId", JWApplication.getWorkSession().getUser().getUserId()));
				Utente readOne = (Utente) criteria.uniqueResult();
				if(readOne != null) {
					if(readOne.getPassword().equalsIgnoreCase(vecchiaPassword)) {
						readOne.setPassword(nuovaPassword);
						s.update(readOne);
						return null;
					}
					return "Password vecchia non corretta";
				}
				return "Utente non trovato";
			}
		});
	}

	public Utente registerUser(final String nome, final String cognome, final String email, final String password, final String telefono) {
		Utente ret = executeInTransaction(new TransactionBlock<Utente>() {
			@Override
			public Utente run() throws Throwable {
				Session s = manager.getSession();
				Utente utente = new Utente();
				utente.setUserId(email);
				utente.setFirstName(nome);
				utente.setSurname(cognome);
				utente.setEmail(email);
				utente.setPassword(password);
				utente.setTelefono1(telefono);
				utente.setGruppo("registrati");
				s.save(utente);
				
				return utente;
			}
		});
		
		if(ret != null)
			JWApplication.getWorkSession().setUser(ret);
		
		return ret;
	}

	public synchronized void addVisitCount() {
		executeInTransaction(new TransactionBlock<Void>() {
			@Override
			public Void run() throws Throwable {
				Session s = manager.getSession();
				Visite visite = (Visite) s.createCriteria(Visite.class)
						.add(Restrictions.eq("idVisite", 1L))
						.uniqueResult();
				
				if(visite != null) {
					visite.setContatore(visite.getContatore()+1);
					s.update(visite);
				}
				else {
					visite=new Visite();
					s.save(visite);
				}
				
				return null;
			}
		});
	}
}
