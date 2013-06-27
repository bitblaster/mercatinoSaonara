package it.mozzicato.mercatino.client.moduli;

import it.infracom.jwolfgwt.client.Shell;
import it.infracom.jwolfgwt.client.ui.controls.*;
import it.infracom.jwolfgwt.client.ui.controls.edit.EditWindow;
import it.infracom.jwolfgwt.client.ui.modules.IModule;
import it.infracom.jwolfgwt.client.utils.ClientHelper;
import it.infracom.jwolfgwt.client.utils.criteria.CriteriaUtils;
import it.infracom.jwolfgwt.client.utils.datasource.AbstractCrudDataSource;
import it.mozzicato.mercatino.client.*;
import it.mozzicato.mercatino.client.datasource.*;
import it.mozzicato.mercatino.persistent.*;

import java.util.*;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.smartgwt.client.data.*;
import com.smartgwt.client.types.*;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.widgets.*;
import com.smartgwt.client.widgets.events.*;
import com.smartgwt.client.widgets.grid.*;
import com.smartgwt.client.widgets.layout.*;
import com.smartgwt.client.widgets.viewer.DetailViewer;

public class ModuloPrenotazioni extends Canvas implements IModule {
	private ButtonPosition buttonPosition = ButtonPosition.RIGHT;
	protected Canvas title;
	protected FormattedListGrid listGridPrenotazioni = null;
	protected ButtonPanel buttonPanelPrenotazioni;
	protected FormattedListGrid listGridVendita = null;
	protected ButtonPanel buttonPanelVendita;
	protected FormattedListGrid listGridWishList = null;
	protected ButtonPanel buttonPanelWishList;
	private long auditId;
	protected PrenotazioneDataSource prenotazioniDs = GWT.create(PrenotazioneDataSource.class);
	protected CopiaDataSource venditaDs = GWT.create(CopiaDataSource.class);
	protected WishListDataSource wishListDs = GWT.create(WishListDataSource.class);
	private ImgButton btnHelp;
	private ImgButton btnVenduto;
	

	@Override
	public void initModule(Map<Object, Object> pars) {
		title = createTitle();

		prenotazioniDs.setImplicitCriteria(
			CriteriaUtils.and(
				CriteriaUtils.eq("utente.userId", Shell.getInstance().getCurrentUser().getUserId()),
				CriteriaUtils.isNull("dataRimozione")
			)
		);
		listGridPrenotazioni = createListGrid(prenotazioniDs);
		listGridPrenotazioni.getField(prenotazioniDs.descrizioneField.getName()).setHidden(true);
		listGridPrenotazioni.getField(prenotazioniDs.autoreField.getName()).setHidden(true);
		buttonPanelPrenotazioni = createButtonPanel(ButtonPanel.REFRESH | ButtonPanel.DELETE | ButtonPanel.INFO);

		venditaDs.prenotataDaUserField.setHidden(true);
		venditaDs.isbnField.setHidden(false);
		venditaDs.titoloField.setHidden(false);
		venditaDs.descrizioneField.setHidden(false);
		venditaDs.autoreField.setHidden(false);
		venditaDs.setJoinFields("utente", "libro");
		venditaDs.setImplicitCriteria(
			CriteriaUtils.and(
				CriteriaUtils.eq("utente.userId", Shell.getInstance().getCurrentUser().getUserId()),
				CriteriaUtils.isNull("dataVendita"),
				CriteriaUtils.isNull("dataRimozione")
			)
		);
		listGridVendita = createListGrid(venditaDs);
		listGridVendita.getField(venditaDs.descrizioneField.getName()).setHidden(true);
		listGridVendita.getField(venditaDs.autoreField.getName()).setHidden(true);
		buttonPanelVendita = createButtonPanel(ButtonPanel.REFRESH | ButtonPanel.DELETE | ButtonPanel.INFO);
		btnVenduto = buttonPanelVendita.createButton(MercatinoImageBundle.VENDUTO.path(), "Venduto");
		buttonPanelVendita.addMember(btnVenduto, 2);

		wishListDs.setImplicitCriteria(CriteriaUtils.eq("utente.userId", Shell.getInstance().getCurrentUser().getUserId()));
		listGridWishList = createListGrid(wishListDs);
		buttonPanelWishList = createButtonPanel(ButtonPanel.REFRESH | ButtonPanel.DELETE | ButtonPanel.INFO);

		Layout container;
		container = new VLayout();
		container.setWidth100();
		container.setHeight100();
		composeModule(container);

		createHandlers();

		addChild(container);
		
		String cookie = Cookies.getCookie("helpMiePrenotazioniDone");
		
		if(cookie == null) {
			GregorianCalendar cal = new GregorianCalendar();
			cal.add(Calendar.MONTH, 10);
			Cookies.setCookie("helpMiePrenotazioniDone", "true", cal.getTime());
			showHelp();
		}
	}
	
	protected Canvas createTitle() {
		Label label = new Label();
		label.setWidth100();
		label.setHeight(30);
		label.setPadding(10);
		label.setAlign(Alignment.CENTER);
		label.setValign(VerticalAlignment.CENTER);
		label.setWrap(false);
		label.setContents("<span style=\"color: '#4682B4'; font-size: 12pt; font-weight:bold;\"><span class=\"moduleTitle\">Le mie prenotazioni</span></span>");
		
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

	protected AdvancedForm createForm() {
		AdvancedForm ret = new AdvancedForm();
		ret.setIsGroup(true);
		ret.setNumCols(4);
		ret.setDataSource(listGridPrenotazioni.getDataSource());
		ret.setPadding(15);
		ret.setLayoutAlign(VerticalAlignment.BOTTOM);
		ret.setLayoutAlign(Alignment.CENTER);
		return ret;
	}

	protected ButtonPanel createButtonPanel(int buttons) {
		ButtonPanel.Orientation orientation;
		if (buttonPosition == ButtonPosition.LEFT || buttonPosition == ButtonPosition.RIGHT)
			orientation = ButtonPanel.Orientation.VERTICAL;
		else
			orientation = ButtonPanel.Orientation.HORIZONTAL;

		String buttonPrefix = getClass().getName();
		buttonPrefix = buttonPrefix.substring(buttonPrefix.lastIndexOf('.') + 1);

		return new ButtonPanel(buttonPrefix, orientation, 
			ButtonPanel.IconStyle.MEDIUM, 
			buttons);
	}

	protected FormattedListGrid createListGrid(DataSource datasource) {
		FormattedListGrid ret = new FormattedListGrid();
		ret.setWidth100();
		ret.setHeight100();
		ret.setShowFilterEditor(false);
		ret.setHeaderHeight(20);
		ret.setCanSort(false);
		ret.setAutoFetchData(true);
		if(datasource instanceof AbstractCrudDataSource)
			((AbstractCrudDataSource) datasource).setAuditId(auditId);
		ret.setDataSource(datasource);
		return ret;
	}

	protected void composeModule(Layout container) {
		container.addMember(title);
		
		Label label = new Label("<span class=\"moduleSubTitle\">Copie prenotate</span>");
		label.setAutoHeight();
		container.addMember(label);
		Layout entityPanel = composeEntityPanel(listGridPrenotazioni, buttonPanelPrenotazioni);
		container.addMember(entityPanel);
		container.addMember(ClientHelper.createVerticalSpacer("20"));
		label = new Label("<span class=\"moduleSubTitle\">Copie in vendita</span>");
		label.setAutoHeight();
		container.addMember(label);
		entityPanel = composeEntityPanel(listGridVendita, buttonPanelVendita);
		container.addMember(entityPanel);
		container.addMember(ClientHelper.createVerticalSpacer("20"));
		label = new Label("<span class=\"moduleSubTitle\">Libri che cerchi</span>");
		label.setAutoHeight();
		container.addMember(label);
		entityPanel = composeEntityPanel(listGridWishList, buttonPanelWishList);
		container.addMember(entityPanel);
	}

	private Layout composeEntityPanel(ListGrid listGrid, ButtonPanel buttonPanel) {
		Layout entityPanel;
		if (buttonPosition == ButtonPosition.TOP || buttonPosition == ButtonPosition.BOTTOM)
			entityPanel = new VLayout();
		else
			entityPanel = new HLayout();

		entityPanel.setWidth100();
		entityPanel.setHeight100();
		entityPanel.setMembersMargin(10);
			
		if(buttonPosition == ButtonPosition.TOP || buttonPosition == ButtonPosition.LEFT)
			entityPanel.addMember(buttonPanel);

		listGrid.setAlign(Alignment.CENTER);
		entityPanel.addMember(listGrid);
		
		if(buttonPosition == ButtonPosition.BOTTOM || buttonPosition == ButtonPosition.RIGHT)
			entityPanel.addMember(buttonPanel);

		return entityPanel;
	}

	protected void createHandlers() {
		
		createHandlers(buttonPanelPrenotazioni);
		createHandlers(buttonPanelVendita);
		createHandlers(buttonPanelWishList);
		
		btnHelp.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				showHelp();
			}
		});
		
		btnVenduto.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				String messaggio = "Confermi la vendita di questa copia (verr&agrave; rimossa dall'elenco e tutte le prenotazioni annullate)?";
				ClientHelper.showConfirm(messaggio, new BooleanCallback() {

					@Override
					public void execute(Boolean value) {
						if (value != null && value) {
							chiudiVendita(true);
						}
					}
				});
			}
		});
	}
	
	protected void chiudiVendita(boolean venduto) {
		ListGridRecord selectedRecord = listGridVendita.getSelectedRecord();
		if(selectedRecord != null) {
			if(venduto)
				selectedRecord.setAttribute(venditaDs.dataVenditaField.getName(), new Date());
			else
				selectedRecord.setAttribute(venditaDs.dataRimozioneField.getName(), new Date());
			
			venditaDs.updateData(selectedRecord, new DSCallback() {
				
				@Override
				public void execute(DSResponse response, Object rawData, DSRequest request) {
					listGridVendita.reFetch();
				}
			});
		}	
	}

	protected void showHelp() {
		Window helpWindow = HelpWindow.createHelpWindow("Le mie prenotazioni", GWT.getHostPageBaseURL() + "helpMiePrenotazioni.html");
		helpWindow.show();				
	}

	protected void createHandlers(final ButtonPanel buttonPanel) {
		if (buttonPanel.getRefreshButton() != null && !buttonPanel.getRefreshButton().isDisabled()) {
			buttonPanel.getRefreshButton().addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					onClickRefresh(buttonPanel);
				}
			});
		}

		if (buttonPanel.getDetailButton() != null && !buttonPanel.getDetailButton().isDisabled()) {
			buttonPanel.getDetailButton().addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					onClickDetail(buttonPanel);
				}
			});
		}

		if (buttonPanel.getDeleteButton() != null && !buttonPanel.getDeleteButton().isDisabled()) {
			buttonPanel.getDeleteButton().addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					onClickDelete(buttonPanel);
				}
			});
		}
	}
	
	protected void onClickDelete(ButtonPanel buttonPanel) {
		ListGrid listGrid=null;
		String messaggio=null;
		if(buttonPanel == buttonPanelPrenotazioni) {
			listGrid = listGridPrenotazioni;
			messaggio = "Sei sicuro di voler eliminare la prenotazione?";
		}
		else if(buttonPanel == buttonPanelVendita) {
			listGrid = listGridVendita;
			messaggio = "Sei sicuro di voler eliminare la copia dalla vendita?<br/>Se &egrave; stata prenotata verr&agrave; inviata una mail ai relativi utenti per aggiornarli.";
		}
		else if(buttonPanel == buttonPanelWishList) {
			listGrid = listGridWishList;
			messaggio = "Sei sicuro di voler eliminare il libro dalla lista dei libri che cerchi?";
		}
		
		final ListGrid listGridScelta = listGrid;
		if (listGridScelta.getSelectedRecord() != null) {
			ClientHelper.showConfirm(messaggio, new BooleanCallback() {

				@Override
				public void execute(Boolean value) {
					if (value != null && value) {
						if(listGridScelta == listGridWishList)
							listGridScelta.removeSelectedData();
						else if(listGridScelta == listGridVendita)
							chiudiVendita(false);
						else if(listGridScelta == listGridPrenotazioni)
							chiudiPrenotazione();
					}
				}
			});
		}
		else {
			ClientHelper.showMessage(ClientHelper.T.selectOneRow());
		}

	}

	protected void chiudiPrenotazione() {
		ListGridRecord selectedRecord = listGridPrenotazioni.getSelectedRecord();
		if(selectedRecord != null) {
			selectedRecord.setAttribute(prenotazioniDs.dataRimozioneField.getName(), new Date());
			prenotazioniDs.updateData(selectedRecord, new DSCallback() {
				
				@Override
				public void execute(DSResponse response, Object rawData, DSRequest request) {
					listGridPrenotazioni.reFetch();
				}
			});
		}		
	}

	protected void onClickDetail(ButtonPanel buttonPanel) {
		ListGrid listGrid=null;
		if(buttonPanel == buttonPanelPrenotazioni) {
			listGrid = listGridPrenotazioni;
		}
		else if(buttonPanel == buttonPanelVendita) {
			listGrid = listGridVendita;
		}
		else if(buttonPanel == buttonPanelWishList) {
			listGrid = listGridWishList;
		}
		
		if (listGrid.getSelectedRecord() != null) {
			DetailViewer detailViewer = new FormattedDetailViewer(listGrid);
			detailViewer.setWidth100();
			detailViewer.setMargin(15);
			//detailViewer.setDatetimeFormatter(DateDisplayFormat.TOEUROPEANSHORTDATETIME);
			//detailViewer.setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATE);
			detailViewer.setEmptyMessage(ClientHelper.T.warningRecord());
			detailViewer.setDataSource(listGrid.getDataSource());
			detailViewer.setData(new Record[] {listGrid.getSelectedRecord()});

			EditWindow editWindow = new EditWindow(this, getTitle(), detailViewer);
			editWindow.show();
		}
		else {
			ClientHelper.showMessage(ClientHelper.T.selectOneRow());
		}
	}

	protected void onClickRefresh(ButtonPanel buttonPanel) {
		ListGrid listGrid=null;
		if(buttonPanel == buttonPanelPrenotazioni) {
			listGrid = listGridPrenotazioni;
		}
		else if(buttonPanel == buttonPanelVendita) {
			listGrid = listGridVendita;
		}
		else if(buttonPanel == buttonPanelWishList) {
			listGrid = listGridWishList;
		}
		
		listGrid.invalidateCache();
		listGrid.fetchData();
	}

	@Override
	public void refreshModule(Map<Object, Object> pars) {
		prenotazioniDs.setImplicitCriteria(CriteriaUtils.eq("utente.userId", Shell.getInstance().getCurrentUser().getUserId()));
		venditaDs.setImplicitCriteria(CriteriaUtils.eq("utente.userId", Shell.getInstance().getCurrentUser().getUserId()));
		wishListDs.setImplicitCriteria(CriteriaUtils.eq("utente.userId", Shell.getInstance().getCurrentUser().getUserId()));
	}

	@Override
	public void canClose(BooleanCallback callback) {
		callback.execute(true);
	}

	@Override
	public void finalizeModule() {
	}
}
