package it.mozzicato.mercatino.server;

import it.infracom.jwolf.*;

import javax.servlet.*;

public final class StartUp implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        JWApplication.init(new MercatinoConfiguration());
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        JWApplication.destroy();
    }

}
