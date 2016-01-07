package eu.freme.common.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.io.ClassPathResource;

public class FREMEStarter {

	public static void main(String[] args){
		startPackageFromClasspath("package.xml", args);
	}
	
	public static ConfigurableApplicationContext startPackageFromClasspath(String packagePath, String[] args){
		ClassPathResource config = new ClassPathResource(packagePath);
		return SpringApplication.run(config, args);
	}

	public static ConfigurableApplicationContext startPackageFromClasspath(String packagePath){
		return startPackageFromClasspath(packagePath, new String[]{});
	}
}
