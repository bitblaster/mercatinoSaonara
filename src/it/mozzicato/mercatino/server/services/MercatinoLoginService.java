package it.mozzicato.mercatino.server.services;

import it.infracom.jwolf.JWApplication;
import it.infracom.jwolf.authorization.*;
import it.infracom.jwolf.extensions.hibernate.connection.HibernateManager;
import it.infracom.jwolf.extensions.ram.service.RamLoginService;
import it.infracom.jwolf.service.*;
import it.mozzicato.mercatino.persistent.Utente;
import it.mozzicato.mercatino.server.MercatinoConfiguration;

import java.util.Map;

import org.hibernate.*;
import org.hibernate.criterion.Restrictions;

public class MercatinoLoginService extends AbstractLoginService<HibernateManager> {
	RamLoginService xmlLoginService = JWApplication.getService(MercatinoConfiguration.SERVIZIO_LOGIN_XML);
	
	@Override
	public void authorizeService(String serviceClassName, String serviceMethodName) {
		
	}

	@Override
	public Map<String, UiProfile> getUiProfileMap() {
		return xmlLoginService.getUiProfileMap();
	}

	@Override
	public long audit(String operation, String elementName, String elementId, String parameters, long parentAuditId) {
		return 0;
	}

	@Override
	protected User findUser(final String userId, final String password) {
		return executeInTransaction(new TransactionBlock<User>() {
			@Override
			public User run() throws Throwable {
				Session s = manager.getSession();
				Criteria criteria = s.createCriteria(Utente.class);
				criteria.add(Restrictions.eq("userId", userId));
				criteria.add(Restrictions.eq("password", password));
				Utente ret = (Utente) criteria.uniqueResult();
				
				if(ret != null && ret.getGruppo() != null) {
					ICrudService<Group> xmlCrudService = JWApplication.getCrudService(MercatinoConfiguration.SERVIZIO_CRUD_AUTH_XML, Group.class);
					Group profiloApp = xmlCrudService.readOne(ret.getGruppo());
					if(profiloApp != null)
						ret.getGroups().add(profiloApp);
				}
				return ret;
			}
		});
	}

}
