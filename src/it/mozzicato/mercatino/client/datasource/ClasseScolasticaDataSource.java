package it.mozzicato.mercatino.client.datasource;

import it.infracom.jwolfgwt.client.utils.datasource.GeneratedCrudDataSource;
import it.mozzicato.mercatino.persistent.ClasseScolastica;

import com.smartgwt.client.data.SortSpecifier;
import com.smartgwt.client.data.fields.*;
import com.smartgwt.client.types.SortDirection;

public class ClasseScolasticaDataSource extends GeneratedCrudDataSource<ClasseScolastica> {
    public final DataSourceIntegerField idField = new DataSourceIntegerField("idClasse","Id");
	public final DataSourceTextField descrizioneField = new DataSourceTextField("descrizione","Descrizione");
	
	public ClasseScolasticaDataSource() {
		setImplicitSorting(new SortSpecifier(descrizioneField.getName(), SortDirection.ASCENDING));
	}
}