package it.mozzicato.mercatino.client;

import it.infracom.jwolfgwt.client.*;
import it.infracom.jwolfgwt.client.ui.UserBox;
import it.infracom.jwolfgwt.client.ui.formatters.DateCellFormatter;
import it.infracom.jwolfgwt.client.ui.menu.MenuTreeNode;
import it.infracom.jwolfgwt.client.ui.modules.IModule;
import it.infracom.jwolfgwt.client.utils.ClientHelper;
import it.mozzicato.mercatino.client.moduli.ModuloLibri;

import java.util.*;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.smartgwt.client.types.*;
import com.smartgwt.client.util.*;
import com.smartgwt.client.widgets.*;
import com.smartgwt.client.widgets.events.*;
import com.smartgwt.client.widgets.layout.*;
import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.client.widgets.tree.events.LeafClickEvent;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Mercatino extends Shell {

	protected Layout menuUserPanel;

	@Override
	public void onModuleInit() {
		Application.setShell(Shell.getInstance());

//		setLoginScreen(new MercatinoLoginScreen());

		GregorianCalendar cal = new GregorianCalendar();
		System.out.println(cal.getTime());
		
		Img imgLogo = new Img(MercatinoImageBundle.LOGO.path());
		imgLogo.setImageType(ImageStyle.CENTER);

		// TODO: questa MERDA � necessaria perch� quella CIOFECA di Internet Explorer non capisce le dimensioni dei png trasparenti! 
		imgLogo.setImageWidth(130);
		imgLogo.setImageHeight(131);

		setLogo(imgLogo);
		
//		DateUtil.setDateInputFormatter(DateCellFormatter.DATETIME);
		DateUtil.setDateParser(DateCellFormatter.DATETIME);
		DateUtil.setShortDateDisplayFormat(DateDisplayFormat.TOEUROPEANSHORTDATE);
		DateUtil.setNormalDateDisplayFormat(DateDisplayFormat.TOEUROPEANSHORTDATE);
		DateUtil.setShortDateDisplayFormatter(DateCellFormatter.DATETIME);
		DateUtil.setNormalDateDisplayFormatter(DateCellFormatter.DATETIME);

		ClientHelper.APPLICATION_NAME = "Mercatino Libri";
	}
	
	@Override
	protected UserBox createUserBox() {
		return new MercatinoUserBox();
	}
	
	@Override
	protected void createMenuPanel() {
		super.createMenuPanel();
		mainMenu.setOpenerIconWidth(1);
		mainMenu.setShowOpener(false);
		mainMenu.setShowHeader(false);
		mainMenu.setShowOpenIcons(false);
		mainMenu.setShowConnectors(false);
		mainMenu.setBorder("none");
		mainMenu.setIconSize(32);
		mainMenu.setCellHeight(48);
		mainMenu.setWidth(200);
	}
	
	@Override
	protected void composeStructure() {
		createMenuUserPanel();
		
		menuUserPanel.setShowResizeBar(false);
		mainLayout.setStyleName("headerBanner");
		menuAndContentPanel.addMember(menuUserPanel);
		VLayout vContainer = new VLayout();
		Img titolo = new Img(MercatinoImageBundle.TITOLO.path());
		titolo.setWidth(682);
		titolo.setHeight(64);
		titolo.setLayoutAlign(Alignment.CENTER);
		vContainer.addMember(titolo);
		containerPanel.setWidth100();
		vContainer.addMember(containerPanel);
		menuAndContentPanel.addMember(vContainer);
		mainLayout.addMember(menuAndContentPanel);
	}
    
	protected void createMenuUserPanel() {
		menuUserPanel = new VLayout(0);
		
		if (mainMenu != null) {
			mainMenu.setCanFocus(false);
			menuUserPanel.setHeight100();
			//menuUserPanel.setWidth(getLogo().getImageWidth());
			menuUserPanel.setWidth(Math.max(200, getLogo().getImageWidth()));
			menuUserPanel.setMinWidth(getLogo().getImageWidth());
			menuUserPanel.setShowResizeBar(true);
			menuUserPanel.setMargin(0);
			menuUserPanel.setPadding(0);
			
			menuUserPanel.addMember(getLogo());
			menuUserPanel.addMember(ClientHelper.createVerticalSpacer("20"));
//			Label linea = new Label("<hr />");
//			linea.setWidth100();
			Img linea = new Img(MercatinoImageBundle.LINEA.path());
			linea.setAutoHeight();
			menuUserPanel.addMember(linea);
			menuUserPanel.addMember(mainMenu);			
//			linea = new Label("<hr />");
//			linea.setWidth100();
			linea = new Img(MercatinoImageBundle.LINEA.path());
			linea.setAutoHeight();
			menuUserPanel.addMember(linea);
			menuUserPanel.addMember(userBox);
		}
	}
	
	// TODO METODO DA TOGLIERE, HO DISATTIVATO IL DEBUG DEGLI SCRIPT PER NON VISUALIZZARE ERRORI!
	@Override
	public void onModuleLoad() {
		super.onModuleLoad();
		SC.setEnableJSDebugger(false);
		GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
			
			@Override
			public void onUncaughtException(Throwable e) {
				e.printStackTrace();
			}
		});
	}
	
	public void goToMainScreen() {
		mainMenu.invalidateCache();
		mainMenu.fetchData();
		mainLayout.show();
		
		String cookie = Cookies.getCookie("welcomeDone");
			
		if(getCurrentUser() == null && cookie == null) {
			GregorianCalendar cal = new GregorianCalendar();
			cal.add(Calendar.MONTH, 10);
			Cookies.setCookie("welcomeDone", "true", cal.getTime());
			showWelcomeWindow();
		}
		else
			addUIModule(ModuloLibri.class.getName(), "menuLibri", "Ricerca e inserimento Libri", MercatinoImageBundle.LIBRI_16.path(), false, null, false, null);

	}
	
	private void showWelcomeWindow() {
		Window welcomeWindow = HelpWindow.createHelpWindow("Benvenuto!", GWT.getHostPageBaseURL() + "welcomeScreen.html");
		welcomeWindow.addCloseClickHandler(new CloseClickHandler() {
			
			@Override
			public void onCloseClick(CloseClickEvent event) {
				addUIModule(ModuloLibri.class.getName(), "menuLibri", "Ricerca e inserimento Libri", MercatinoImageBundle.LIBRI_16.path(), false, null, false, null);
			}
		});
		welcomeWindow.show();
	}

	@Override
	protected void onMainMenuClick(LeafClickEvent event) {
		TreeNode tnode = event.getLeaf();
		MenuTreeNode node = new MenuTreeNode(tnode.getJsObj());

		if ("HELP".equals(node.getUri()))
			showWelcomeWindow();
		else
			super.onMainMenuClick(event);
	}
	
	@Override
	public IModule addModule(String pluginName, String instanceName, String caption, Map<Object, Object> parameters) {
		return null;
	}

	@Override
	public <T extends IModule> T addModule(Class<T> moduleClazz, String instanceName, String caption, Map<Object, Object> parameters) {
		return null;
	}

	@Override
	public IModule addModule(ModuleInfo moduleInfo) {
		return null;
	}

	@Override
	public ModuleInfo getModuleInfo(IModule module) {
		return null;
	}

	@Override
	public void closeModule(IModule module) {}

	@Override
    public void goToLoginScreen() {
		onAuthenticationSuccess(null);
//        super.goToLoginScreen();
//        
//        if(!GWT.isScript()) {
//            loginScreen.setValues("roby", "roby");
//        }
    }
	
	@Override
	public void onAuthenticationFailure(Throwable caught) {
//		super.onAuthenticationFailure(caught);
		LoginWindow loginWindow = new LoginWindow();
		loginWindow.show();
	}
	
	@Override
	public void reload() {
		LinkedList<IModule> modules = containerPanel.getModules();
		for (IModule module : modules) {
			if(module instanceof ModuloLibri)
				module.refreshModule(null);
			else
				containerPanel.removeModule(module);
		}
		callLoginService();
//		super.reload();
	}
}
