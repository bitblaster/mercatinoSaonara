package it.mozzicato.mercatino.server.services;

import it.infracom.jwolf.JWApplication;
import it.infracom.jwolf.authorization.User;
import it.infracom.jwolf.extensions.hibernate.service.HibernateCrudService;
import it.infracom.jwolf.utils.criteria.*;
import it.infracom.jwolf.utils.criteria.Criterion;
import it.mozzicato.mercatino.persistent.*;

import java.util.*;

import org.hibernate.*;
import org.hibernate.criterion.*;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class LibroCrudService extends HibernateCrudService {
	
	@Override
	protected List<Object> internalRead(List<Criterion> criterions, Pagination pagination, Sorting<Object> sorting) {
		List<Object> ret =super.internalRead(criterions, pagination, sorting);
		
		User user = JWApplication.getWorkSession().getUser();
		if(user != null && parameters.get().get("LEGGI_WISHLIST") != null) {
			Session s = manager.getSession();
			Criteria critWish = s.createCriteria(WishList.class);
			critWish.add(Restrictions.eq("utente.userId", user.getUserId()));
			critWish.addOrder(Order.asc("libro.isbn"));
			List<WishList> listaWishList = critWish.list();
			ArrayList<Libro> libriWishList = new ArrayList<Libro>();
			
			for (WishList wishList : listaWishList) {
				libriWishList.add(wishList.getLibro());
			}
			
			if(libriWishList.size() > 0) {
				for (Libro libro : (List<Libro>) (List) ret) {
					int indice = Collections.binarySearch(libriWishList, libro);
					if(indice >= 0) {
						libro.setWishLists(new ArrayList<WishList>(1));
						libro.getWishLists().add(listaWishList.get(indice));
					}
				}
			}
		}
		
		return ret;
	}
	
	public void prova() {
		Session s = manager.getSession();
		Criteria ret = s.createCriteria(Libro.class);
	    ret.createAlias("wishLists", "wishLists", CriteriaSpecification.LEFT_JOIN);
		ret.add(Restrictions.eq("wishLists.utente.userId", "roby"));
		List<Libro> listaPrenotati = ret.list();
		List<String> isbnPrenotati = new ArrayList<String>(listaPrenotati.size());
		for (Libro libro : listaPrenotati) {
			isbnPrenotati.add(libro.getIsbn());
		}
		ret = s.createCriteria(Libro.class);
		ret.add(Restrictions.not(Restrictions.in("isbn", isbnPrenotati)));
		List<Libro> listaNonPrenotati = ret.list();
		
		listaPrenotati.addAll(listaNonPrenotati);
		
		for (Libro libro : listaPrenotati) {
			System.out.println(libro.getIsbn());
		}
	}
}
