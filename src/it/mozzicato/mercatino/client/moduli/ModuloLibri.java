package it.mozzicato.mercatino.client.moduli;

import it.infracom.jwolf.authorization.User;
import it.infracom.jwolfgwt.client.*;
import it.infracom.jwolfgwt.client.ui.controls.*;
import it.infracom.jwolfgwt.client.ui.modules.formfactory.*;
import it.infracom.jwolfgwt.client.ui.modules.masterdetail.MasterDetailNode;
import it.infracom.jwolfgwt.client.utils.*;
import it.infracom.jwolfgwt.client.utils.criteria.CriteriaUtils;
import it.infracom.jwolfgwt.client.utils.datasource.*;
import it.mozzicato.mercatino.client.*;
import it.mozzicato.mercatino.client.datasource.*;
import it.mozzicato.mercatino.client.services.*;
import it.mozzicato.mercatino.persistent.Copia;

import java.util.*;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.smartgwt.client.data.*;
import com.smartgwt.client.types.*;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.widgets.*;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.*;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.*;
import com.smartgwt.client.widgets.grid.events.*;
import com.smartgwt.client.widgets.layout.*;

public class ModuloLibri extends MercatinoMasterDetailModule {
	private IMercatinoServiceWebAsync service = GWT.create(IMercatinoServiceWeb.class);

	private LibroDataSource dsLibri = GWT.create(LibroDataSource.class);
	private CopiaDataSource dsCopie = GWT.create(CopiaDataSource.class);
	private MasterDetailNode detailCopie;
	private ImgButton btnWishlist;
	private ImgButton btnPrenota;

	protected boolean disabilitaPrenotazione;

	private EntitySelectItem cboClassi;

	private DynamicForm classiForm;

	private ImgButton btnHelp;

	public ModuloLibri() {
		dsLibri.setParameter("LEGGI_WISHLIST", "SI");
		dsCopie.setParameter("LEGGI_PRENOTAZIONI", "SI");
		dsCopie.setJoinFields("utente");
		
		//imposto il nodo di default (master) al datasource persona
		getMasterNode().setDatasource(dsLibri);
		//obbligatorio impostare il name
		getMasterNode().setName("libri");
		//� il titolo della pagina
		getMasterNode().setTitle("Libri");
		//		getMasterNode().setCreatable(false);
		//		getMasterNode().setUpdatable(fprotected void composeModule(Layout container) {alse);
		//		getMasterNode().setDeletable(false);
		getMasterNode().setRefreshable(false);

		//idem per indirizzo, solo che creo un nuovo nodo.
		detailCopie = new MasterDetailNode();
		detailCopie.setDatasource(dsCopie);
		detailCopie.setName("copie");
		detailCopie.setTitle("Copie disponibili");
		detailCopie.setDeletable(false);
		detailCopie.setFormFactory(new CopiaFormFactory());

		detailCopie.addFieldRelation(dsCopie.isbnField, dsLibri.isbnField);

		//aggiungo il nodo al modulo
		addDetailNode(detailCopie);

		classiForm = new DynamicForm();
		classiForm.setWidth(170);
		classiForm.setMargin(Integer.valueOf(10));
		ClasseScolasticaDataSource dsClassi = GWT.create(ClasseScolasticaDataSource.class);

		cboClassi = new EntitySelectItem("classe", "Classe", dsClassi, dsClassi.descrizioneField.getName());

		cboClassi.setAllowEmptyValue(true);
		cboClassi.setWrapTitle(false);
		cboClassi.setWidth(160);
		//		ComboBoxItem cboClassi2 = new ComboBoxItem("classe", "Classe");
		//		ClasseScolasticaDataSource dsClassi2 = GWT.create(ClasseScolasticaDataSource.class);
		//		cboClassi2.setOptionDataSource(dsClassi2);
		//		cboClassi2.setDisplayField(dsClassi2.descrizioneField.getName());
		//		cboClassi2.setAllowEmptyValue(true);
		classiForm.setFields(cboClassi);

		String cookie = Cookies.getCookie("helpLibri");

		if (cookie == null) {
			GregorianCalendar cal = new GregorianCalendar();
			cal.add(Calendar.MONTH, 10);
			Cookies.setCookie("helpLibri", "true", cal.getTime());
			showHelp();
		}
	}

	@Override
	protected ButtonPanel createButtonPanel(MasterDetailNode node) {
		ButtonPanel ret = super.createButtonPanel(node);

		if (node == getMasterNode()) {
			btnWishlist = ret.createButton(MercatinoImageBundle.WISHLIST_ADD.path(), "Notifica quando disponibile");
			ret.addButton(btnWishlist, "wishlist");
			btnWishlist.setDisabled(true);
		}
		else if (node == detailCopie) {
			btnPrenota = ret.createButton(MercatinoImageBundle.PRENOTA_ADD.path(), "Prenota");
			ret.addButton(btnPrenota, "prenota");
			btnPrenota.setDisabled(true);
		}

		return ret;
	}

	@Override
	protected void createGlobalHandlers() {
		super.createGlobalHandlers();
		cboClassi.addChangedHandler(new ChangedHandler() {

			@Override
			public void onChanged(ChangedEvent event) {
				if (event.getValue() == null) {
					dsLibri.setJoinFields("disciplina");
					dsLibri.setImplicitCriteria(null);
				}
				else {
					dsLibri.setJoinFields("disciplina", "classi");
					dsLibri.setImplicitCriteria(CriteriaUtils.eq("classi.idClasse", event.getValue()));
				}

				getMasterNode().getListGrid().invalidateCache();
				getMasterNode().getListGrid().fetchData();
			}
		});

		btnHelp.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				showHelp();
			}
		});
	}

	protected void showHelp() {
		Window helpWindow = HelpWindow.createHelpWindow("Ricerca e inserimento Libri", GWT.getHostPageBaseURL() + "helpLibri.html");
		helpWindow.show();
	}

	@Override
	protected ListGrid createListGrid(MasterDetailNode node) {
		ListGrid ret = super.createListGrid(node);
		if (node == getMasterNode())
			ret.setFilterOnKeypress(true);
		else {
			ret.setShowFilterEditor(false);
			ret.setCanSort(false);
		}

		return ret;
	}

	@Override
	protected Criteria createMasterCriteria(Record masterRecord, LinkedHashMap<DataSourceField, DataSourceField> detailFieldRels) {
		Criteria ret = super.createMasterCriteria(masterRecord, detailFieldRels);
//		ret.addCriteria(new AdvancedCriteria(dsCopie.dataVenditaField.getName(), OperatorId.IS_NULL));
//		ret.addCriteria(new AdvancedCriteria(dsCopie.dataRimozioneField.getName(), OperatorId.IS_NULL));
//		return ret;
		return CriteriaUtils.and(
			ret,
			CriteriaUtils.isNull(dsCopie.dataVenditaField.getName()),
			CriteriaUtils.isNull(dsCopie.dataRimozioneField.getName()));
	}
	@Override
	protected void createHandlers(MasterDetailNode node) {
		super.createHandlers(node);

		if (node == getMasterNode()) {
			btnWishlist.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					aggiungiWishlist();
				}
			});
		}
		else if (node == detailCopie) {
			btnPrenota.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					prenota();
				}
			});

			dsCopie.addListener(new DataSourceListener<Copia>() {
				@Override
				public void onFetch(FetchResult<Copia> result) {
					btnWishlist.setDisabled(btnWishlist.getDisabled() || result.getFetchedList().size() > 0);

					disabilitaPrenotazione = false;
					for (Copia copia : result.getFetchedList()) {
						if (copia.getPrenotazioni() != null && copia.getPrenotazioni().size() > 0)
							disabilitaPrenotazione = true;
					}
				}
			});

			detailCopie.getListGrid().addSelectionChangedHandler(new SelectionChangedHandler() {

				@Override
				public void onSelectionChanged(SelectionEvent event) {
					onCopieChangedSelection(event);
				}
			});
		}
	}

	protected void composeModule(Layout container) {
		super.composeModule(container);
		container.addMember(classiForm, 1);
	}

	@Override
	protected Canvas createTitle() {
		Label label = new Label();
		label.setWidth100();
		label.setHeight(30);
		label.setPadding(10);
		label.setAlign(Alignment.CENTER);
		label.setValign(VerticalAlignment.CENTER);
		label.setWrap(false);
		label.setContents("<span style=\"color: '#4682B4'; font-size: 12pt; font-weight:bold;\"><span class=\"moduleTitle\">Libri</span></span>");

		HLayout hLayout = new HLayout();
		hLayout.addMember(label);
		btnHelp = new ImgButton();
		btnHelp.setShowRollOver(false);
		btnHelp.setShowDownIcon(false);
		btnHelp.setSrc(MercatinoImageBundle.HELP.path());
		btnHelp.setSize(32);
		hLayout.addMember(btnHelp);
		return hLayout;
	}

	@Override
	protected void refreshMasterHeader() {}

	protected void onCopieChangedSelection(SelectionEvent event) {
		User user = Shell.getInstance().getCurrentUser();
		String userIdCopia = event.getRecord().getAttribute(dsCopie.utenteField.getName());
		btnPrenota.setDisabled(
			disabilitaPrenotazione || 
			!event.getState() || 
			event.getRecord().getAttribute(dsCopie.prenotataDaUserField.getName()) != null ||
			(userIdCopia != null && user != null && userIdCopia.equals(user.getUserId())));
	}

	@Override
	protected void onMasterListGridChangedSelection(SelectionEvent event) {
		btnWishlist.setDisabled(!event.getState() || event.getRecord().getAttribute(dsLibri.miaWishList.getName()) != null);
		super.onMasterListGridChangedSelection(event);
	}

	protected void prenota() {
		final ListGridRecord copiaSelezionata = detailCopie.getListGrid().getSelectedRecord();
		if (detailCopie.getForm() == null || copiaSelezionata == null) {
			ClientHelper.showWarning("Selezionare un libro dalla lista per poterlo prenotare");
			return;
		}

		ClientHelper.showConfirm(
			"Prenotando questa copia il sistema ti renderà disponibili le informazioni di contatto del venditore (e viceversa).<br />Confermando la prenotazione ti impegni a contattare al più presto il venditore per procedere all'acquisto.<br />Confermi la prenotazione?",
			new BooleanCallback() {

				@Override
				public void execute(Boolean value) {
					String attribute = copiaSelezionata.getAttribute(dsCopie.idCopiaField.getName());
					System.out.println(attribute);
					service.prenota(ClientHelper.getAttributeAsInteger(copiaSelezionata, dsCopie.idCopiaField.getName()), new JWAsyncCallback<Boolean>() {
						@Override
						public void onSuccess(Boolean result) {
							ClientHelper.showMessage("Prenotazione effettuata con successo");
							((FormattedListGrid) detailCopie.getListGrid()).reFetch();
							//TODO: refreshare il modulo delle prenotazioni se c'è
						}
					});
				}
			});
	}

	protected void aggiungiWishlist() {
		final ListGridRecord libroSelezionato = getMasterNode().getListGrid().getSelectedRecord();
		if (getMasterNode().getForm() == null || libroSelezionato == null) {
			ClientHelper.showMessage(ClientHelper.T.selectRow());
			return;
		}

		service.wishList(libroSelezionato.getAttribute(dsLibri.isbnField.getName()), new JWAsyncCallback<Boolean>() {
			@Override
			public void onSuccess(Boolean result) {
				ClientHelper.showMessage("Libro aggiunto all'elenco dei libri che cerchi");
				((FormattedListGrid) getMasterNode().getListGrid()).reFetch();
			}
		});
	}

	@Override
	protected void showUpdatePopup(MasterDetailNode node) {
		if (node != detailCopie || node.getForm() != null && node.getListGrid().getSelectedRecord() != null)
			super.showUpdatePopup(node);
		else
			ClientHelper.showWarning("Selezionare una copia di un libro dalla lista per poterla prenotare");
	}

	@Override
	public void refreshModule(Map<Object, Object> pars) {
		ButtonPanel libriBP = getMasterNode().getButtonPanel();
		ButtonPanel copieBP = detailCopie.getButtonPanel();

		Application.getShell().profileCanvas(libriBP.getUiElementId("insert"), libriBP.getCreateButton());
		Application.getShell().profileCanvas(libriBP.getUiElementId("update"), libriBP.getUpdateButton());
		Application.getShell().profileCanvas(libriBP.getUiElementId("delete"), libriBP.getDeleteButton());
		Application.getShell().profileCanvas(copieBP.getUiElementId("insert"), copieBP.getCreateButton());
		Application.getShell().profileCanvas(copieBP.getUiElementId("update"), copieBP.getUpdateButton());
		Application.getShell().profileCanvas(copieBP.getUiElementId("delete"), copieBP.getDeleteButton());
		Application.getShell().profileCanvas(libriBP.getUiElementId("wishlist"), btnWishlist);
		Application.getShell().profileCanvas(copieBP.getUiElementId("prenota"), btnPrenota);

		((FormattedListGrid) getMasterNode().getListGrid()).reFetch();
		//		detailCopie.getListGrid().invalidateCache();
		//		detailCopie.getListGrid().fetchData();
	}

	class CopiaFormFactory extends AbstractFormFactory {

		@Override
		public void customizeInsertFormFields(FieldsList fieldsList) {
			customizeUpdateFormFields(fieldsList);
		}

		@Override
		public void customizeUpdateFormFields(FieldsList fieldsList) {
			fieldsList.get(dsCopie.condizioneField.getName()).setEndRow(true);
			TextAreaItem txtNote = new TextAreaItem(dsCopie.noteField.getName(), dsCopie.noteField.getTitle());
			fieldsList.put(dsCopie.noteField.getName(), txtNote);
			FormItem formItem = fieldsList.get(dsCopie.dataInserimentoField.getName());
			formItem.setVisible(false);
		}

	}

	@Override
	public void canClose(BooleanCallback callback) {
		callback.execute(false);
	}
}
