// Creato il 15/ott/2010
package it.mozzicato.mercatino.client;

import it.infracom.jwolfgwt.client.*;
import it.infracom.jwolfgwt.client.service.*;
import it.infracom.jwolfgwt.client.ui.*;
import it.infracom.jwolfgwt.client.utils.*;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.*;
import com.smartgwt.client.widgets.events.*;
import com.smartgwt.client.widgets.layout.HLayout;

/**
 * @author Roberto Mozzicato
 *
 */
public class MercatinoUserBox extends UserBox {
	
	protected Label userIdLabel;
	private ImgButton btnLogin;

	@Override
	protected void initControls() {
		setStyleName("userBox");
		ImgButton btnlog = new ImgButton();
		btnlog.setWidth(24);
		btnlog.setHeight(24);
		btnlog.setShowSelectedIcon(false);
		btnlog.setShowRollOver(false);
		btnlog.setShowRollOverIcon(false);
		btnlog.setShowDown(false);
		btnlog.setShowDownIcon(false);
		btnlog.setShowFocused(false);
		btnlog.setShowFocusedIcon(false);
		btnlog.setSrc(ImageBundle.LOGOUT.path());
		btnlog.setHoverWrap(false);
		btnlog.setTooltip("Esci");
		btnLogout=btnlog;
		btnLogout.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				IWebLoginServiceAsync loginServiceWeb = GWT.create(IWebLoginService.class);
				loginServiceWeb.logout(new JWAsyncCallback<Void>() {
					@Override
					public void onSuccess(Void result) {
						currentUser = null;
						refreshView();
						//Shell.getInstance().goToLoginScreen();
						Application.getShell().reload();
					}
				});
			}
		});
		
		btnLogin = new ImgButton();
		btnLogin.setWidth(27);
		btnLogin.setHeight(24);
		btnLogin.setShowSelectedIcon(false);
		btnLogin.setShowRollOver(false);
		btnLogin.setShowRollOverIcon(false);
		btnLogin.setShowDown(false);
		btnLogin.setShowDownIcon(false);
		btnLogin.setShowFocused(false);
		btnLogin.setShowFocusedIcon(false);
		btnLogin.setSrc(MercatinoImageBundle.LOGIN.path());
		btnLogin.setHoverWrap(false);
		btnLogin.setTooltip("Esci");
		btnLogin.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				LoginWindow loginWindow = new LoginWindow();
				loginWindow.show();
			}
		});
		
		userLabel = new Label();
        userLabel.setStyleName("userDescription");
				
		userIdLabel = new Label();
		userIdLabel.setStyleName("userIdDescription");
	}
	
	@Override
	protected void compose() {
		HLayout primaRiga = new HLayout();
		primaRiga.addMember(btnLogout);
		primaRiga.addMember(btnLogin);
		primaRiga.addMember(ClientHelper.createHorizontalSpacer("5"));
		//primaRiga.setHeight100();
		userIdLabel.setWidth100();
		userIdLabel.setLayoutAlign(VerticalAlignment.CENTER);
//		userIdLabel.setHeight(22);
//		primaRiga.setAlign(VerticalAlignment.BOTTOM);
		//userIdLabel.setHeight100();
		primaRiga.addMember(userIdLabel);
		//btnLogout.setAutoFit(true);

		HLayout secondaRiga = new HLayout();
		userLabel.setWidth100();
		secondaRiga.addMember(userLabel);

		Label copyrightLabel = new Label("<br /><span class='applicationName'>Comitato Genitori Saonara</span><br /><span class='copyrightString'>2013 &copy; Roberto Mozzicato - v. " + Version.VERSION + "</span>");
		copyrightLabel.setAutoHeight();
		//setMargin(10);
		setMembers(primaRiga, secondaRiga, copyrightLabel);
	}
	
	@Override
	protected void refreshView() {
		
		if(currentUser == null) {
			userIdLabel.setContents("Utente non autenticato");
			userLabel.setContents("");
			btnLogout.hide();
			btnLogin.show();
		}
		else {
			userIdLabel.setContents(currentUser.getUserId());
			userLabel.setContents(currentUser.getFullName());
			btnLogout.show();
			btnLogin.hide();
		}
	}
}