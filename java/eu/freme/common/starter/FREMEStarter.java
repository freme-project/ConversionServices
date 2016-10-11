package eu.freme.common.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ClassPathResource;

/**
 * Start this class to load a package configuration "package.xml" that must be placed in the classpath.
 * 
 * @author jnehring
 *
 */
public class FREMEStarter {

	public static void main(String[] args){
		String pack = "package.xml";
		if( args.length > 0){
			pack = args[0];
		}
		startPackageFromClasspath(pack, args);
	}
	
	/**
	 * Start FREME package from XML configuration file.
	 * 
	 * @param packagePath Path to the xml configuration file.
	 * @param args Command line arguments passed to the spring application context.
	 * @return
	 */
	public static ConfigurableApplicationContext startPackageFromClasspath(String packagePath, String[] args){
		ClassPathResource config = new ClassPathResource(packagePath);
		return SpringApplication.run(config, args);
	}

	/**
	 * Start FREME package from XML configuration file.
	 * 
	 * @param packagePath Path to the xml configuration file.
	 * @return
	 */
	public static ConfigurableApplicationContext startPackageFromClasspath(String packagePath){
		return startPackageFromClasspath(packagePath, new String[]{});
	}
}
