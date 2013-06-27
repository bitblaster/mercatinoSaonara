package it.mozzicato.mercatino.client.datasource;

import it.infracom.jwolfgwt.client.ui.controls.DataSourceSelectField;
import it.infracom.jwolfgwt.client.ui.formatters.NumberCellFormatter;
import it.infracom.jwolfgwt.client.utils.*;
import it.infracom.jwolfgwt.client.utils.datasource.*;
import it.mozzicato.mercatino.client.MercatinoImageBundle;
import it.mozzicato.mercatino.persistent.Libro;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.data.*;
import com.smartgwt.client.data.fields.*;
import com.smartgwt.client.types.*;

public class LibroDataSource extends GeneratedCrudDataSource<Libro> {
    public final DataSourceTextField isbnField = new DataSourceTextField("isbn","Codice ISBN");
	public final DataSourceTextField titoloField = new DataSourceTextField("titolo","Titolo");
	public final DataSourceTextField descrizioneField = new DataSourceTextField("descrizione","Descrizione");
	public final DataSourceTextField autoreField = new DataSourceTextField("autore","Autore");
	public final DataSourceSelectField disciplinaField = new DataSourceSelectField("disciplina.idDisciplina","Disciplina");	
	public final DataSourceTextField editoreField = new DataSourceTextField("editore","Editore");
	public final DataSourceIntegerField annoField = new DataSourceIntegerField("anno","Anno");	
	public final DataSourceFloatField prezzoField = new DataSourceFloatField("prezzo","Prezzo");
	@ClientOnly
	public final DataSourceImageField miaWishList = new DataSourceImageField("miaWishList", "Notifica");
	
	public LibroDataSource() {
		isbnField.setHidden(false);
		FieldHelper.setOperator(titoloField, OperatorId.ICONTAINS);
		FieldHelper.setOperator(descrizioneField, OperatorId.ICONTAINS);
		FieldHelper.setOperator(autoreField, OperatorId.ICONTAINS);
		FieldHelper.setOperator(editoreField, OperatorId.ICONTAINS);
		
		DisciplinaDataSource dsDisciplina = GWT.create(DisciplinaDataSource.class);
		disciplinaField.setDatasource(dsDisciplina);
		disciplinaField.setDisplayField(dsDisciplina.descrizioneField.getName());
		
		if(GWT.isClient())
			FieldHelper.setNumberCellFormatter(prezzoField, NumberCellFormatter.GROUPED_FLOAT);
		miaWishList.setCanFilter(false);
		miaWishList.setCanEdit(false);
		miaWishList.setCanSortClientOnly(true);
		miaWishList.setWidth(60);
		
		setJoinFields("disciplina");
		setImplicitSorting(new SortSpecifier(titoloField.getName(), SortDirection.ASCENDING));
	}
	
	@Override
	public void dtoToRecord(Libro from, Record to) {
		if(from.getWishLists() != null && from.getWishLists().size() > 0)
			to.setAttribute(miaWishList.getName(), MercatinoImageBundle.WISHLIST.path());
	}
}