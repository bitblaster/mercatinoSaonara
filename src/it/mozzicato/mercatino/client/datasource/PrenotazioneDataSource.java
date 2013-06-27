package it.mozzicato.mercatino.client.datasource;

import it.infracom.jwolf.utils.EnumUtils;
import it.infracom.jwolfgwt.client.utils.FieldHelper;
import it.infracom.jwolfgwt.client.utils.datasource.GeneratedCrudDataSource;
import it.mozzicato.mercatino.persistent.Prenotazione;
import it.mozzicato.mercatino.persistent.enums.CondizioneCopia;

import com.smartgwt.client.data.*;
import com.smartgwt.client.data.fields.*;
import com.smartgwt.client.types.SortDirection;

public class PrenotazioneDataSource extends GeneratedCrudDataSource<Prenotazione> {
	public final DataSourceIntegerField idPrenotazioneField = new DataSourceIntegerField("idPrenotazione","ID");
//	public final DataSourceTextField idCopiaField = new DataSourceTextField("copia.idCopia","ID Copia");
	public final DataSourceTextField isbnField = new DataSourceTextField("copia.libro.isbn","Codice ISBN");
	public final DataSourceTextField titoloField = new DataSourceTextField("copia.libro.titolo","Titolo");
	public final DataSourceTextField descrizioneField = new DataSourceTextField("copia.libro.descrizione","Descrizione");
	public final DataSourceTextField autoreField = new DataSourceTextField("copia.libro.autore","Autore");
	public final DataSourceEnumField condizioneField = new DataSourceEnumField("copia.condizione","Condizione");
	public final DataSourceDateTimeField dataPrenotazioneField = new DataSourceDateTimeField("dataPrenotazione", "Data Prenotazione");
	public final DataSourceDateTimeField dataRimozioneField = new DataSourceDateTimeField("dataRimozione", "Data Rimozione");
	public final DataSourceTextField fullNameVenditoreField = new DataSourceTextField("copia.utente.fullName", "Venditore");
	public final DataSourceTextField emailVenditoreField = new DataSourceTextField("copia.utente.email", "Email venditore");
	public final DataSourceTextField telVenditoreField = new DataSourceTextField("copia.utente.telefono1", "Tel. venditore");
	
	public PrenotazioneDataSource() {
		dataRimozioneField.setHidden(true);
	    condizioneField.setValueMap(EnumUtils.enumToValueMap(CondizioneCopia.values()));
		FieldHelper.setWidth(isbnField, 95);
		FieldHelper.setWidth(condizioneField, 70);
		FieldHelper.setWidth(telVenditoreField, 95);
			    
		setJoinFields("copia", "copia.libro", "copia.utente");
		setImplicitSorting(new SortSpecifier(dataPrenotazioneField.getName(), SortDirection.ASCENDING));
    }
	
	@Override
	public void recordToDto(Record from, Prenotazione to) {
//		to.setUtente(null);
//		to.setCopia(null);
	}
}
