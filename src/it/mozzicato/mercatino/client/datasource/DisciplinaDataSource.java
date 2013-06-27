package it.mozzicato.mercatino.client.datasource;

import it.infracom.jwolfgwt.client.utils.datasource.GeneratedCrudDataSource;
import it.mozzicato.mercatino.persistent.*;

import com.smartgwt.client.data.SortSpecifier;
import com.smartgwt.client.data.fields.*;
import com.smartgwt.client.types.SortDirection;

public class DisciplinaDataSource extends GeneratedCrudDataSource<Disciplina> {
    public final DataSourceIntegerField idField = new DataSourceIntegerField("idDisciplina","Id");
	public final DataSourceTextField descrizioneField = new DataSourceTextField("descrizione","Descrizione");
	
	public DisciplinaDataSource() {
		setImplicitSorting(new SortSpecifier(descrizioneField.getName(), SortDirection.ASCENDING));
	}
}