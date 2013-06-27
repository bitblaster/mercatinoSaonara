package it.mozzicato.mercatino.client;

import it.infracom.jwolfgwt.client.ui.ImageBundle;

public class MercatinoImageBundle extends ImageBundle {
	private static final String IMAGE_SUBDIR="images/";
	public static final MercatinoImageBundle LOGO = new MercatinoImageBundle("logo.png");
	public static final MercatinoImageBundle WISHLIST_ADD = new MercatinoImageBundle("wishlist_add.png");
	public static final MercatinoImageBundle WISHLIST = new MercatinoImageBundle("wishlist.png");
	public static final MercatinoImageBundle PRENOTA_ADD = new MercatinoImageBundle("prenota_add.png");
	public static final MercatinoImageBundle PRENOTATO = new MercatinoImageBundle("prenotato.png");
	public static final MercatinoImageBundle PRENOTAZIONI_16 = new MercatinoImageBundle("prenotazioni16.png");
	public static final MercatinoImageBundle PRENOTAZIONI = new MercatinoImageBundle("prenotazioni.png");
	public static final MercatinoImageBundle LOGIN = new MercatinoImageBundle("login.png");
	public static final MercatinoImageBundle LINEA = new MercatinoImageBundle("lineSeparator.png");
	public static final MercatinoImageBundle TITOLO = new MercatinoImageBundle("titolo.png");
	public static final MercatinoImageBundle LIBRI = new MercatinoImageBundle("libri.png");
	public static final MercatinoImageBundle LIBRI_16 = new MercatinoImageBundle("libri16.png");
	public static final MercatinoImageBundle VENDUTO = new MercatinoImageBundle("venduto32.png");
			
	public MercatinoImageBundle(String name) {
		super(name);
	}
	
	@Override
	public String getImageSubDir() {
		return IMAGE_SUBDIR;
	}
}
