package it.mozzicato.mercatino.server;

import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.user.rebind.rpc.*;

public class MercatinoProxyGenerator extends ServiceInterfaceProxyGenerator {
	@Override
	protected ProxyCreator createProxyCreator(JClassType remoteService) {
		return new MercatinoProxyCreator(remoteService);
	}
}
