/***************************************************************************
 * Copyright (C) 2016 iObserve Project (https://www.iobserve-devops.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
package org.iobserve.mobile.server.start;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.iobserve.mobile.server.rest.RestService;

/**
 * Class with a main method that is used to start a monitoring server.
 * 
 * @author Robert Heinrich
 * @author David Monschein
 *
 */
public final class StartServer {

	/**
	 * No instance creation allowed because static main execution.
	 */
	private StartServer() {
	}

	/**
	 * Starts a monitoring server with Jetty.
	 * 
	 * @param args
	 *            Command line arguments which are irrelevant in this case.
	 */
	public static void main(final String[] args) {
		final ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");

		final Server jettyServer = new Server(8182);
		jettyServer.setHandler(context);

		final ServletHolder jerseyServlet = context.addServlet(org.glassfish.jersey.servlet.ServletContainer.class,
				"/*");
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
