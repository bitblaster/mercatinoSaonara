package it.mozzicato.mercatino.client.moduli;

import it.infracom.jwolf.authorization.User;
import it.infracom.jwolfgwt.client.Shell;
import it.infracom.jwolfgwt.client.ui.controls.*;
import it.infracom.jwolfgwt.client.ui.modules.masterdetail.*;
import it.infracom.jwolfgwt.client.utils.ClientHelper;
import it.mozzicato.mercatino.client.*;

public class MercatinoMasterDetailModule extends AbstractMasterDetailModule {
	public MercatinoMasterDetailModule() {
		setButtonPosition(ButtonPosition.RIGHT);
	}
	
	@Override
	protected void showCreatePopup(MasterDetailNode node) {
		User currentUser = Shell.getInstance().getCurrentUser();
		
		if(currentUser == null) {
			LoginWindow loginWindow = new LoginWindow();
			loginWindow.show();
		}
		else
			super.showCreatePopup(node);
	}
}
