package it.mozzicato.mercatino.client;

import it.infracom.jwolf.authorization.User;
import it.infracom.jwolfgwt.client.login.BasicLoginScreen;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.fields.LinkItem;
import com.smartgwt.client.widgets.layout.LayoutSpacer;

public class MercatinoLoginScreen extends BasicLoginScreen {
	private Window parentWindow;
	protected LinkItem btnRegister;
	protected LinkItem btnLostPassword;
	
	public MercatinoLoginScreen(Window parentWindow) {
		this.parentWindow = parentWindow;
	}
	
	@Override
	protected void createControls() {
		super.createControls();
		
		userItem.setTitle("Email");
		userItem.setSelectOnFocus(true);
		formLogin.setAutoFocus(true);
		
//		btnRegister = new LinkItem();
//        btnRegister.setTitle("Registrati");
//        btnRegister.setWidth(100);
////        btnRegister.setColSpan(2);
//        btnRegister.setAlign(Alignment.CENTER);

	    btnRegister = new LinkItem("link");
	    btnRegister.setWidth(100);
//	    btnRegister.setTitle("LinkItem");
	    btnRegister.setShowTitle(false);
	    btnRegister.setLinkTitle("Registrati");
	    btnRegister.setAlign(Alignment.CENTER);
        
        btnLostPassword = new LinkItem();
        btnLostPassword.setLinkTitle("Password dimenticata");
        btnLostPassword.setShowTitle(false);
//        btnLostPassword.setTitle("Password dimenticata");
//        btnLostPassword.setWidth(150);
//        btnLostPassword.setColSpan(2);
        btnLostPassword.setAlign(Alignment.CENTER);
        
        
	}
	
	@Override
	protected void compose() {
		formLogin.setFields(userItem, passwordItem, txtError, btnLogin, btnRegister, btnLostPassword);

		addMember(new LayoutSpacer());
		addMember(formLogin);
		addMember(new LayoutSpacer());	
	}
	
	@Override
	protected void createHandlers() {
		super.createHandlers();

		btnRegister.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
            @Override
			public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
        		doRegister();
            }   
        });
	}

	@Override
	protected void onLoginSuccess(User user) {
		super.onLoginSuccess(user);
		parentWindow.markForDestroy();
	}
	
	protected void doRegister() {
		parentWindow.markForDestroy();
		RegisterWindow registerScreen = new RegisterWindow();
		registerScreen.show();
	}
}
