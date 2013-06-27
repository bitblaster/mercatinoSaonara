package it.mozzicato.mercatino.client;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.*;
import com.smartgwt.client.widgets.events.*;
import com.smartgwt.client.widgets.layout.VLayout;

public class LoginWindow extends Window {
	protected MercatinoLoginScreen loginScreen;
	
	public LoginWindow() {
		loginScreen = new MercatinoLoginScreen(this);
		VLayout vLayout = new VLayout();
		
		Label label = new Label("Per poter gestire le proprie copie ed effettuare prenotazioni è necessario essere registrati.<br />Se sei già registrato inserisci i tuoi dati, altrimenti clicca su Registrati per registrarti.");
		label.setMargin(5);
		label.setWidth100();
		label.setAutoHeight();
		label.setLayoutAlign(Alignment.CENTER);
		vLayout.addMember(label);
		vLayout.addMember(loginScreen);
		
		addItem(vLayout);
		setIsModal(true);
		setShowModalMask(true);
//		setAutoSize(true);
		setSize("400", "300");
		setShowMinimizeButton(false);
		setTitle("Login");
		centerInPage();
		
		addCloseClickHandler(new CloseClickHandler() {
			@Override
			public void onCloseClick(CloseClickEvent event) {
				LoginWindow.this.markForDestroy();				
			}
		});
	}
}
