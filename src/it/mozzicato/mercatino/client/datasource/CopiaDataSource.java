package it.mozzicato.mercatino.client.datasource;

import it.infracom.jwolf.utils.EnumUtils;
import it.infracom.jwolfgwt.client.utils.FieldHelper;
import it.infracom.jwolfgwt.client.utils.datasource.GeneratedCrudDataSource;
import it.mozzicato.mercatino.client.MercatinoImageBundle;
import it.mozzicato.mercatino.persistent.Copia;
import it.mozzicato.mercatino.persistent.enums.CondizioneCopia;

import com.smartgwt.client.data.*;
import com.smartgwt.client.data.fields.*;
import com.smartgwt.client.types.SortDirection;

public class CopiaDataSource extends GeneratedCrudDataSource<Copia> {
	public final DataSourceIntegerField idCopiaField = new DataSourceIntegerField("idCopia","ID");
	public final DataSourceTextField isbnField = new DataSourceTextField("libro.isbn","Codice ISBN");
	public final DataSourceTextField titoloField = new DataSourceTextField("libro.titolo","Titolo");
	public final DataSourceTextField descrizioneField = new DataSourceTextField("libro.descrizione","Descrizione");
	public final DataSourceTextField autoreField = new DataSourceTextField("libro.autore","Autore");
	public final DataSourceEnumField condizioneField = new DataSourceEnumField("condizione","Condizione");
	public final DataSourceDateTimeField dataInserimentoField = new DataSourceDateTimeField("dataInserimento", "Data Inserimento");
	public final DataSourceDateTimeField dataVenditaField = new DataSourceDateTimeField("dataVendita", "Data Vendita");
	public final DataSourceDateTimeField dataRimozioneField = new DataSourceDateTimeField("dataRimozione", "Data Rimozione");
	public final DataSourceTextField noteField = new DataSourceTextField("note", "Note");
	public final DataSourceTextField utenteField = new DataSourceTextField("utente.userId", "Utente");	
//	@ClientOnly
//	public final DataSourceImageField miaPrenotazione = new DataSourceImageField("miaPrenotazione", "Prenotato");
	public final DataSourceIntegerField numPrenotazioniField = new DataSourceIntegerField("numPrenotazioni", "Num. Prenotazioni");
	public final DataSourceImageField prenotataDaUserField = new DataSourceImageField("prenotataDaUser", "Prenotato da me");
	
	public CopiaDataSource() {
		isbnField.setHidden(true);
		titoloField.setHidden(true);
		descrizioneField.setHidden(true);
		autoreField.setHidden(true);
		utenteField.setHidden(true); 
	    
		condizioneField.setValueMap(EnumUtils.enumToValueMap(CondizioneCopia.values()));
		
		FieldHelper.setWidth(isbnField, 95);
		FieldHelper.setWidth(condizioneField, 70);
		
		numPrenotazioniField.setCanFilter(false);
		numPrenotazioniField.setCanEdit(false);
		numPrenotazioniField.setCanSortClientOnly(true);
		FieldHelper.setWidth(numPrenotazioniField, 100);
		
		prenotataDaUserField.setCanFilter(false);
		prenotataDaUserField.setCanEdit(false);
		prenotataDaUserField.setCanSortClientOnly(true);
		prenotataDaUserField.setWidth(95);
		
		dataVenditaField.setHidden(true);
		dataRimozioneField.setHidden(true);
		
		setImplicitSorting(new SortSpecifier(dataInserimentoField.getName(), SortDirection.ASCENDING));
    }
	
	@Override
	public void dtoToRecord(Copia from, Record to) {
//		if(from.getPrenotazioni() != null && from.getPrenotazioni().size() > 0)
//			to.setAttribute(miaPrenotazione.getName(), MercatinoImageBundle.PRENOTATO.path());
		if(from.isPrenotataDaUser())
			to.setAttribute(prenotataDaUserField.getName(), MercatinoImageBundle.PRENOTATO.path());
		else
			to.setAttribute(prenotataDaUserField.getName(), (String) null);
	}
}
