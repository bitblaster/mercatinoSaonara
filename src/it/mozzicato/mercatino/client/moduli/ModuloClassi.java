package it.mozzicato.mercatino.client.moduli;

import it.infracom.jwolfgwt.client.ui.modules.AbstractEntityModule;
import it.mozzicato.mercatino.client.datasource.ClasseScolasticaDataSource;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.data.DataSource;

public class ModuloClassi extends AbstractEntityModule {

	@Override
	public String getTitle() {
		return "Classi scolastiche";
	}
	
	@Override
	protected DataSource createListGridDataSource() {
		return GWT.create(ClasseScolasticaDataSource.class);
	}
}
