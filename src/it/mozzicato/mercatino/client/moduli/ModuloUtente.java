package it.mozzicato.mercatino.client.moduli;

import it.infracom.jwolfgwt.client.Shell;
import it.infracom.jwolfgwt.client.ui.modules.IModule;
import it.infracom.jwolfgwt.client.utils.*;
import it.mozzicato.mercatino.client.*;
import it.mozzicato.mercatino.client.services.*;
import it.mozzicato.mercatino.persistent.Utente;

import java.util.*;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.smartgwt.client.types.*;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.widgets.*;
import com.smartgwt.client.widgets.events.*;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.*;
import com.smartgwt.client.widgets.form.validator.*;
import com.smartgwt.client.widgets.layout.*;

public class ModuloUtente extends Canvas implements IModule {
	private IMercatinoServiceWebAsync service = GWT.create(IMercatinoServiceWeb.class);
	
	protected Canvas title;
	private DynamicForm formInfo;
	private TextItem txtEmail;
	private TextItem txtNome;
	private TextItem txtCognome;
	private TextItem txtTelefono;
	private DynamicForm formPassword;
	private PasswordItem txtVecchiaPwd;
	private PasswordItem txtNuovaPwd;
	private PasswordItem txtConfermaPwd;
	private ButtonItem btnCambiaPassword;
	private ButtonItem btnModificaDati;

	private ImgButton btnHelp;

	@Override
	public void initModule(Map<Object, Object> pars) {
		title = createTitle();

		formInfo = new DynamicForm();
		formInfo.setGroupTitle("Informazioni utente");
		formInfo.setIsGroup(true);
		formInfo.setPadding(10);
		formInfo.setWidth(310);
		formInfo.setColWidths("100", "*");
		formInfo.setWrapItemTitles(false);
		formInfo.setErrorOrientation(FormErrorOrientation.RIGHT);
		txtEmail = new TextItem("email", "Email");
		txtEmail.setDisabled(true);
		txtNome = new TextItem("nome", "Nome");
		txtNome.setRequired(true);
		txtCognome = new TextItem("cognome", "Cognome");
		txtCognome.setRequired(true);
		txtTelefono = new TextItem("telefono", "Telefono");
		RegExpValidator telValidator = new RegExpValidator("^\\+?[0-9- ]+$");
		telValidator.setErrorMessage("Numero di telefono non valido");
		txtTelefono.setValidators(telValidator);
		txtTelefono.setValidateOnExit(true);
//		txtTelefono.setRequired(true);
		btnModificaDati = new ButtonItem("cambiapwd", "Modifica dati");
		btnModificaDati.setAlign(Alignment.CENTER);
		btnModificaDati.setColSpan(2);
		formInfo.setFields(txtEmail, txtNome, txtCognome, txtTelefono, btnModificaDati);

		formPassword = new DynamicForm();
		formPassword.setGroupTitle("Cambia password");
		formPassword.setIsGroup(true);
		formPassword.setPadding(10);
		formPassword.setWidth(310);
		formPassword.setColWidths("100", "*");
		formPassword.setWrapItemTitles(false);
		formPassword.setErrorOrientation(FormErrorOrientation.RIGHT);
		txtVecchiaPwd = new PasswordItem("vecchiapwd", "Vecchia password");
		txtVecchiaPwd.setRequired(true);
		txtNuovaPwd = new PasswordItem("nuovapwd", "Nuova password");
		txtNuovaPwd.setRequired(true);
		txtConfermaPwd = new PasswordItem("confermapwd", "Conferma password");
		txtConfermaPwd.setRequired(true);
		MatchesFieldValidator matchesFieldValidator = new MatchesFieldValidator();
		matchesFieldValidator.setErrorMessage("La password nuova e quella di conferma non corrispondono");
		matchesFieldValidator.setOtherField(txtNuovaPwd.getName());
		txtConfermaPwd.setValidators(matchesFieldValidator);
		txtConfermaPwd.setValidateOnExit(true);
		
		btnCambiaPassword = new ButtonItem("cambiapwd", "Imposta nuova password");
		btnCambiaPassword.setAlign(Alignment.CENTER);
		btnCambiaPassword.setColSpan(2);
		formPassword.setFields(txtVecchiaPwd, txtNuovaPwd, txtConfermaPwd, btnCambiaPassword);

		Layout container;
		container = new VLayout();
		container.setWidth100();
		container.setHeight100();
		composeModule(container);

		setInitialValues();
		createHandlers();

		addChild(container);
		
		String cookie = Cookies.getCookie("helpUtente");
		
		if(cookie == null) {
			GregorianCalendar cal = new GregorianCalendar();
			cal.add(Calendar.MONTH, 10);
			Cookies.setCookie("helpUtente", "true", cal.getTime());
			showHelp();
		}
	}

	private void setInitialValues() {
		Utente utente = (Utente) Shell.getInstance().getCurrentUser();
		txtEmail.setValue(utente.getEmail());
		txtNome.setValue(utente.getFirstName());
		txtCognome.setValue(utente.getSurname());
		txtTelefono.setValue(utente.getTelefono1());
	}

	protected Canvas createTitle() {
		Label label = new Label();
		label.setWidth100();
		label.setHeight(30);
		label.setPadding(10);
		label.setAlign(Alignment.CENTER);
		label.setValign(VerticalAlignment.CENTER);
		label.setWrap(false);
		label.setContents("<span style=\"color: '#4682B4'; font-size: 12pt; font-weight:bold;\"><span class=\"moduleTitle\">Gestione utente</span></span>");
		
		HLayout hLayout = new HLayout();
		hLayout.addMember(label);
		hLayout.setHeight100();
		hLayout.setAutoHeight();
		btnHelp = new ImgButton();
		btnHelp.setShowRollOver(false);
		btnHelp.setShowDownIcon(false);
		btnHelp.setSrc(MercatinoImageBundle.HELP.path());
		btnHelp.setSize(32);
		hLayout.addMember(btnHelp);
		return hLayout;
	}

	protected void composeModule(Layout container) {
		container.addMember(title);

		container.addMember(ClientHelper.createVerticalSpacer("40"));
		HLayout hLayout = new HLayout();
		hLayout.setWidth100();
		hLayout.setAutoHeight();
		hLayout.addMember(ClientHelper.createHorizontalSpacer("20"));
		hLayout.addMember(formInfo);
		container.addMember(hLayout);
		container.addMember(ClientHelper.createVerticalSpacer("40"));
		hLayout = new HLayout();
		hLayout.setWidth100();
		hLayout.setAutoHeight();
		hLayout.addMember(ClientHelper.createHorizontalSpacer("20"));
		hLayout.addMember(formPassword);
		container.addMember(hLayout);
	}

	protected void createHandlers() {
		btnModificaDati.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
			@Override
			public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				modificaDati();
			}
		});

		btnCambiaPassword.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
			@Override
			public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				cambiaPassword();
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
		Window helpWindow = HelpWindow.createHelpWindow("Profilo utente", GWT.getHostPageBaseURL() + "helpUtente.html");
		helpWindow.show();
	}

	protected void modificaDati() {
		if(formInfo.validate()) {
			service.modificaDatiUtente(
				txtNome.getValueAsString(), 
				txtCognome.getValueAsString(), 
				txtTelefono.getValueAsString(),
				new JWAsyncCallback<Boolean>() {
					@Override
					public void onSuccess(Boolean result) {
						if(result)
							ClientHelper.showMessage("Informazioni aggiornate correttamente");
						else
							ClientHelper.showWarning("Si &egrave; verificato un errore durante l'aggiornamento delle informazioni");
					}
				});
		}
	}

	protected void cambiaPassword() {
		if(formPassword.validate()) {
			service.cambiaPassword(
				MD5.md5(txtVecchiaPwd.getValueAsString()),
				MD5.md5(txtNuovaPwd.getValueAsString()),
				new JWAsyncCallback<String>() {
					@Override
					public void onSuccess(String result) {
						if(result == null)
							ClientHelper.showMessage("Password modificata correttamente");
						else {
							HashMap<String, String> errors = new HashMap<String, String>();
							errors.put(txtVecchiaPwd.getName(), result);
							formPassword.setErrors(errors, true);
						}
					}
				});
		}
	}

	@Override
	public void refreshModule(Map<Object, Object> pars) {}

	@Override
	public void canClose(BooleanCallback callback) {
		callback.execute(true);
	}

	@Override
	public void finalizeModule() {}
	
	
}
