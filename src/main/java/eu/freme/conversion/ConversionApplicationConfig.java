package eu.freme.conversion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import eu.freme.conversion.etranslate.TranslationConversionService;
import eu.freme.conversion.etranslate.TranslationConversionServiceImpl;
import eu.freme.conversion.rdf.JenaRDFConversionService;
import eu.freme.conversion.rdf.RDFConversionService;

/**
 * @author Jan Nehring - jan.nehring@dfki.de
 */
@Configuration
public class ConversionApplicationConfig {

	@Bean
	public RDFConversionService getRDFConversionService() {
		return new JenaRDFConversionService();
	}

	@Bean
	public TranslationConversionService getTranslationConversionService() {
		return new TranslationConversionServiceImpl();
	}
}
