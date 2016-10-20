/**
 * Copyright © 2015 Agro-Know, Deutsches Forschungszentrum für Künstliche Intelligenz, iMinds,
 * Institut für Angewandte Informatik e. V. an der Universität Leipzig,
 * Istituto Superiore Mario Boella, Tilde, Vistatec, WRIPL (http://freme-project.eu)
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
 */
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
