package eu.freme.common.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import eu.freme.common.FREMECommonConfig;


@SpringBootApplication
@Import(FREMECommonConfig.class)
public class FREMEStarter {

	public static void main(String[] args){
		SpringApplication.run(FREMEStarter.class, args);
	}
}
