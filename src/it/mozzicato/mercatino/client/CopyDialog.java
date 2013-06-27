package it.mozzicato.mercatino.client;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.*;
import com.smartgwt.client.widgets.events.*;

public class CopyDialog {
	public static Window createHelpWindow(String title, String url) {
		HTMLPane pane = new HTMLPane(); 
		pane.setContentsURL(url);
	    pane.setPadding(10);  
	    pane.setOverflow(Overflow.VISIBLE);
	    pane.setWidth100();
	    pane.setHeight100(); 

	    final Window window = new Window();  
	    window.setShowModalMask(true);
	    window.setIsModal(true);
	    window.setDismissOnOutsideClick(true);
	    window.setShowMinimizeButton(false);
	    window.setTitle(title);
	    window.setAutoSize(true);
	    window.setWidth(600);
	    window.setHeight(50);
//	    window.setAutoCenter(true);
	    window.setCanDragReposition(true);  
	    window.setCanDragResize(true);  
	    window.addItem(pane);
	    window.addCloseClickHandler(new CloseClickHandler() {
			
			@Override
			public void onCloseClick(CloseClickEvent event) {
				window.markForDestroy();				
			}
		});
	    window.addResizedHandler(new ResizedHandler() {
			@Override
			public void onResized(ResizedEvent event) {
				window.centerInPage();
			}
		});
	    
	    return window;
	}
}
