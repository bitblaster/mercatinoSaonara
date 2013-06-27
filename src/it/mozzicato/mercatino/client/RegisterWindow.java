package it.mozzicato.mercatino.client;

import it.infracom.jwolfgwt.client.Application;
import it.infracom.jwolfgwt.client.utils.*;
import it.mozzicato.mercatino.client.services.*;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.*;
import com.smartgwt.client.widgets.*;
import com.smartgwt.client.widgets.events.*;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.events.*;
import com.smartgwt.client.widgets.form.fields.*;
import com.smartgwt.client.widgets.form.validator.*;
import com.smartgwt.client.widgets.layout.*;

public class RegisterWindow extends Window {
	final String EMAIL_VALIDATION_REGEX = "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-+]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	protected DynamicForm formRegister;
	//	protected TextItem usernameItem;
	protected TextItem emailItem;
	protected PasswordItem passwordItem;
	protected PasswordItem confirmPasswordItem;
	protected TextItem telephoneItem;
	protected IButton btnRegister;
	protected IButton btnCancel;
	private TextItem nomeItem;
	private TextItem cognomeItem;

	public RegisterWindow() {
		formRegister = new DynamicForm();
		//		formRegister.setWidth(250);
		//formLogin.setHeight("30%");
		//		formRegister.setHeight(150);
		//		formRegister.setPadding(0);
		formRegister.setCanDragResize(false);
		formRegister.setLayoutAlign(Alignment.CENTER);
		//		formRegister.setLayoutAlign(VerticalAlignment.CENTER);
		formRegister.setAutoFocus(true);
		formRegister.setCanFocus(true);
		formRegister.setErrorOrientation(FormErrorOrientation.RIGHT);
		//		formRegister.setOverflow(Overflow.HIDDEN);
		//		formRegister.setBackgroundColor(formBackgroundColor);
		formRegister.addItemKeyPressHandler(new ItemKeyPressHandler() {

			public void onItemKeyPress(ItemKeyPressEvent event) {
				if ("Enter".equals(event.getKeyName())) {
					doRegister();
				}
			}
		});

		emailItem = new TextItem();
		emailItem.setTitle("Email");
		emailItem.setTitleAlign(Alignment.RIGHT);
		emailItem.setWrapTitle(false);
		//		emailItem.setWidth(formRegister.getWidth()-60);
		emailItem.setRequired(true);
		emailItem.setAlign(Alignment.LEFT);
		emailItem.setSelectOnFocus(true);
		RegExpValidator regExpValidator = new RegExpValidator();
		regExpValidator.setExpression(EMAIL_VALIDATION_REGEX);
		regExpValidator.setErrorMessage("Indirizzo email non valido");
		emailItem.setValidators(regExpValidator);
		emailItem.setValidateOnExit(true);

		nomeItem = new TextItem();
		nomeItem.setTitle("Nome");
		nomeItem.setTitleAlign(Alignment.RIGHT);
		nomeItem.setWrapTitle(false);
		nomeItem.setRequired(true);
		nomeItem.setAlign(Alignment.LEFT);

		cognomeItem = new TextItem();
		cognomeItem.setTitle("Cognome");
		cognomeItem.setTitleAlign(Alignment.RIGHT);
		cognomeItem.setWrapTitle(false);
		cognomeItem.setRequired(true);
		cognomeItem.setAlign(Alignment.LEFT);

		passwordItem = new PasswordItem();
		passwordItem.setTitle("Password");
		passwordItem.setTitleAlign(Alignment.RIGHT);
		passwordItem.setWrapTitle(false);
		//		passwordItem.setWidth(formRegister.getWidth()-60);
		passwordItem.setRequired(true);
		passwordItem.setAlign(Alignment.LEFT);

		confirmPasswordItem = new PasswordItem();
		confirmPasswordItem.setTitle("Conferma password");
		confirmPasswordItem.setTitleAlign(Alignment.RIGHT);
		confirmPasswordItem.setWrapTitle(false);
		//		confirmPasswordItem.setWidth(formRegister.getWidth()-60);
		confirmPasswordItem.setRequired(true);
		confirmPasswordItem.setAlign(Alignment.LEFT);
		MatchesFieldValidator matchesFieldValidator = new MatchesFieldValidator();
		matchesFieldValidator.setErrorMessage("Il campo password e quello di conferma non corrispondono");
		matchesFieldValidator.setOtherField(passwordItem.getName());
		confirmPasswordItem.setValidators(matchesFieldValidator);
		confirmPasswordItem.setValidateOnExit(true);

		telephoneItem = new TextItem();
		telephoneItem.setTitle("Telefono");
		telephoneItem.setTitleAlign(Alignment.RIGHT);
		telephoneItem.setWrapTitle(false);
		//		telephoneItem.setWidth(formRegister.getWidth()-60);
		//		telephoneItem.setRequired(true);
		telephoneItem.setAlign(Alignment.LEFT);
		RegExpValidator telValidator = new RegExpValidator("^\\+?[0-9- ]+$");
		telValidator.setErrorMessage("Numero di telefono non valido");
		telephoneItem.setValidators(telValidator);

		telephoneItem.setValidateOnExit(true);

		formRegister.setFields(emailItem, nomeItem, cognomeItem, passwordItem, confirmPasswordItem, telephoneItem);

		btnRegister = new IButton("Registra");
		btnRegister.setWidth(80);
		//		btnRegister.setIcon("[ISOMORPHIC]/geomajas/staticsecurity/key_go.png");
		btnRegister.setLayoutAlign(Alignment.RIGHT);

		btnCancel = new IButton("Annulla");
		btnCancel.setWidth(80);
		//		btnCancel.setIcon("[ISOMORPHIC]/geomajas/staticsecurity/key_go.png");
		btnCancel.setLayoutAlign(Alignment.LEFT);

		HLayout buttonLayout = new HLayout();
		buttonLayout.setHeight(30);
		buttonLayout.setWidth(300);
		buttonLayout.setMembersMargin(10);
		buttonLayout.setLayoutAlign(Alignment.CENTER);

		VLayout loginBtnLayout = new VLayout();
		loginBtnLayout.setWidth("50%");
		loginBtnLayout.addMember(btnRegister);
		buttonLayout.addMember(loginBtnLayout);

		VLayout resetBtnLayout = new VLayout();
		resetBtnLayout.setWidth("50%");
		resetBtnLayout.addMember(btnCancel);
		buttonLayout.addMember(resetBtnLayout);

		VLayout layout = new VLayout();
		layout.setLayoutAlign(Alignment.CENTER);
		layout.setMembersMargin(10);
		layout.setPadding(10);

		layout.addMember(formRegister);
		layout.addMember(buttonLayout);
		//		layout.addMember(new LayoutSpacer());

		addItem(layout);
		setIsModal(true);
		setShowModalMask(true);
		//		setAutoSize(true);
		setSize("350", "250");
		setTitle("Registrazione utente");
		setShowMinimizeButton(false);
		centerInPage();

		createHandlers();
	}

	protected void createHandlers() {
		btnRegister.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				doRegister();
			}
		});

		btnCancel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				doCancel();
			}
		});

	}

	protected void doRegister() {
		if (formRegister.validate()) {
			final IMercatinoWebLoginServiceAsync service = GWT.create(IMercatinoWebLoginService.class);
			service.registerUser(nomeItem.getValueAsString(), cognomeItem.getValueAsString(), emailItem.getValueAsString(), MD5.md5(passwordItem.getValueAsString()),
				telephoneItem.getValueAsString(), new JWAsyncCallback<Boolean>() {
					@Override
					public void onSuccess(Boolean result) {
						if (result) {
							Application.getShell().reload();
							hide();
						}
					}
				});
		}
	}

	protected void doCancel() {
		hide();
	}
}
