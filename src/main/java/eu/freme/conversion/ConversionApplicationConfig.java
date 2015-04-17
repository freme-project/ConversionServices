package eu.freme.conversion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import eu.freme.conversion.rdf.JenaRDFConversionService;
import eu.freme.conversion.rdf.RDFConversionService;

@Configuration
public class ConversionApplicationConfig {

	@Bean
	public RDFConversionService getRDFConversionService(){
		return new JenaRDFConversionService();
	}
}
