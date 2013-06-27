package it.mozzicato.mercatino.server;

import it.infracom.jwolf.*;
import it.infracom.jwolf.authorization.Authorization;
import it.infracom.jwolf.connection.NullManager;
import it.infracom.jwolf.extensions.hibernate.connection.HibernateManager;
import it.infracom.jwolf.extensions.hibernate.service.HibernateCrudService;
import it.infracom.jwolf.extensions.ram.service.*;
import it.infracom.jwolf.extensions.xml.connection.XMLManager;
import it.infracom.jwolf.service.*;
import it.infracom.jwolf.utils.StringUtils;
import it.infracom.jwolf.utils.criteria.Restrictions;
import it.infracom.jwolfgwt.server.service.WebCrudServiceImpl;
import it.mozzicato.mercatino.MailService;
import it.mozzicato.mercatino.persistent.*;
import it.mozzicato.mercatino.server.services.*;

import java.io.*;
import java.util.*;

public class MercatinoConfiguration extends JWConfiguration {
	private static XMLManager MAN_XML_UTENTI;
	private static HibernateManager MAN_HIBERNATE;

	public static ServiceType<HibernateCrudService, HibernateManager> SERVIZIO_CRUD_HIB;
	public static ServiceType<RamLoginService, XMLManager> SERVIZIO_LOGIN_XML;
	public static ServiceType<RamCrudService, XMLManager> SERVIZIO_CRUD_AUTH_XML;	
	public static ServiceType<LibroCrudService, HibernateManager> SERVIZIO_CRUD_LIBRI;
	public static ServiceType<CopiaCrudService, HibernateManager> SERVIZIO_CRUD_COPIE;
	public static ServiceType<PrenotazioneCrudService, HibernateManager> SERVIZIO_CRUD_PRENOTAZIONI;
	public static ServiceType<MercatinoLoginService, HibernateManager> SERVIZIO_LOGIN_HIB;
	public static ServiceType<MercatinoBusinessService, HibernateManager> SERVIZIO_BUSINESS;
	public static ServiceType<MailService, NullManager> SERVIZIO_MAIL;
	
	@Override
	public void onInit() {
		// Configurazione di Log4j
		configureLogger("it/mozzicato/mercatino/resources/log4jConf.properties", 0);

		MAN_HIBERNATE = new HibernateManager("it/mozzicato/mercatino/resources/hibernate.cfg.xml");

		SERVIZIO_CRUD_HIB = ServiceType.defineLocal(HibernateCrudService.class, MAN_HIBERNATE);
		WebCrudServiceImpl.configureHibernateSessionFactory(MAN_HIBERNATE.getSessionFactory());
		WebCrudServiceImpl.configureDefaultCrudServiceType(SERVIZIO_CRUD_HIB);
		
		// Configura il servizio di autenticazione e profilazione di JWolf
		MAN_XML_UTENTI = new XMLManager("it/mozzicato/mercatino/resources/autorizzazioni.xml", Authorization.class);
		MAN_XML_UTENTI.setAutoReload(true);
		
		SERVIZIO_CRUD_AUTH_XML = ServiceType.defineLocal(RamCrudService.class, MAN_XML_UTENTI);
		SERVIZIO_LOGIN_XML = ServiceType.defineLocal(RamLoginService.class, MAN_XML_UTENTI);
//		RamLoginService loginService = JWApplication.getService(SERVIZIO_LOGIN_XML);
//		configureAuthentication(loginService);
		
		SERVIZIO_LOGIN_HIB = ServiceType.defineLocal(MercatinoLoginService.class, MAN_HIBERNATE);
		MercatinoLoginService loginService = JWApplication.getService(SERVIZIO_LOGIN_HIB);
		configureAuthentication(loginService);
		
		SERVIZIO_CRUD_LIBRI = ServiceType.defineLocal(LibroCrudService.class, MAN_HIBERNATE);
		SERVIZIO_CRUD_COPIE = ServiceType.defineLocal(CopiaCrudService.class, MAN_HIBERNATE);
		SERVIZIO_CRUD_PRENOTAZIONI = ServiceType.defineLocal(PrenotazioneCrudService.class, MAN_HIBERNATE);
		
		SERVIZIO_BUSINESS = ServiceType.defineLocal(MercatinoBusinessService.class, MAN_HIBERNATE);
		SERVIZIO_MAIL = ServiceType.defineLocal(MailService.class, NullManager.getInstance());
		Properties props = new Properties();
		try {
			props.load(new FileInputStream(getClass().getClassLoader().getResource("it/mozzicato/mercatino/resources/mail.properties").getFile()));
			MailService.setMailServerProperties(props);
		}
		catch (Exception e) {
			JWApplication.logger().error(StringUtils.stackTraceToString(e));
		}

		JWApplication.setSessionTimeout(900);
	}

	@Override
	public void onDestroy() {
		MAN_HIBERNATE.shutdown();		
	}
	
	public static void main(String[] args) {
		JWApplication.init(new MercatinoConfiguration());
		
		ICrudService<Libro> crudService = JWApplication.getCrudService(MercatinoConfiguration.SERVIZIO_CRUD_HIB, Libro.class);
		crudService.setJoinFields("classi", "disciplina");
		List<Libro> readAll = crudService.read(Restrictions.eq("classi.idClasse", 5));
		int i=1;
		for (Libro libro : readAll) {
			System.out.println(i+") " + libro.getIsbn() + " - " + libro.getDisciplina().getDescrizione());
			for (ClasseScolastica classe : libro.getClassi()) {
				System.out.println("    " + classe.getDescrizione());
			}
			i++;
		}
	}
}
