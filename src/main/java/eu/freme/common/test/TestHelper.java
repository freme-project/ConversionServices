package eu.freme.common.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class TestHelper {

	/**
	 * Returns the base url of the API given the spring application context, e.g. http://localhost:8080
	 * @return
	 */
	public String getAPIBaseUrl(ApplicationContext context){
		String port = context.getEnvironment().getProperty("server.port");
		if( port == null){
			port = "8080";
		}
		return "http://localhost:" + port;
	}
	
	/**
	 * Return the username of the administrator user of the REST API.
	 * 
	 * @param context
	 * @return
	 */
	public String getAdminUsername(ApplicationContext context){
		return context.getEnvironment().getProperty("admin.username");
	}
	
	/**
	 * Return the password of the administrator user of the REST API.
	 * 
	 * @param context
	 * @return
	 */
	public String getAdminPassword(ApplicationContext context){
		return context.getEnvironment().getProperty("admin.password");
	}
}
