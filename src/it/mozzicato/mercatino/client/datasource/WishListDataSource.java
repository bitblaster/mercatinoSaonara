package it.mozzicato.mercatino.client.datasource;

import it.infracom.jwolfgwt.client.utils.FieldHelper;
import it.infracom.jwolfgwt.client.utils.datasource.GeneratedCrudDataSource;
import it.mozzicato.mercatino.persistent.WishList;

import com.smartgwt.client.data.*;
import com.smartgwt.client.data.fields.*;
import com.smartgwt.client.types.*;

public class WishListDataSource extends GeneratedCrudDataSource<WishList> {
	public final DataSourceTextField idField = new DataSourceTextField("idWishList","id");
	public final DataSourceTextField utenteField = new DataSourceTextField("utente.userId","Utente");
    public final DataSourceTextField isbnField = new DataSourceTextField("libro.isbn","Codice ISBN");
	public final DataSourceTextField titoloField = new DataSourceTextField("libro.titolo","Titolo");
	public final DataSourceTextField descrizioneField = new DataSourceTextField("libro.descrizione","Descrizione");
	public final DataSourceTextField autoreField = new DataSourceTextField("libro.autore","Autore");
	public final DataSourceIntegerField edizioneField = new DataSourceIntegerField("libro.editore","Editore");
	public final DataSourceIntegerField annoField = new DataSourceIntegerField("libro.anno","Anno");	
	public final DataSourceFloatField prezzoField = new DataSourceFloatField("libro.prezzo","Prezzo");
	
	public WishListDataSource() {
		utenteField.setHidden(true);
		FieldHelper.setOperator(titoloField, OperatorId.ICONTAINS);
		FieldHelper.setOperator(descrizioneField, OperatorId.ICONTAINS);
		FieldHelper.setOperator(autoreField, OperatorId.ICONTAINS);
		FieldHelper.setOperator(edizioneField, OperatorId.ICONTAINS);
		
		setJoinFields("libro", "libro.disciplina");
		setImplicitSorting(new SortSpecifier(titoloField.getName(), SortDirection.ASCENDING));

	}
	
	@Override
	public void recordToDto(Record from, WishList to) {
		to.setUtente(null);
		to.setLibro(null);
	}
}