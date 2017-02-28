package org.iobserve.mobile.server.start;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.iobserve.mobile.server.rest.RestService;

/**
 * Class with a main method that is used to start a monitoring server.
 * 
 * @author David Monschein
 * @author Robert Heinrich
 *
 */
public class StartServer {

	/**
	 * Starts a monitoring server with Jetty.
	 * 
	 * @param args
	 *            Command line arguments which are irrelevant in this case.
	 */
	public static void main(String[] args) {
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");

		Server jettyServer = new Server(8182);
		jettyServer.setHandler(context);

		ServletHolder jerseyServlet = context.addServlet(org.glassfish.jersey.servlet.ServletContainer.class, "/*");
		jerseyServlet.setInitOrder(0);

		// Tells the Jersey Servlet which REST service/class to load.
		jerseyServlet.setInitParameter("jersey.config.server.provider.classnames",
				RestService.class.getCanonicalName());

		try {
			jettyServer.start();
			jettyServer.join();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jettyServer.destroy();
		}
	}

}
