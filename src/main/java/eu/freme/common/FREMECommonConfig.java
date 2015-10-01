/**
 * Copyright (C) 2015 Agro-Know, Deutsches Forschungszentrum für Künstliche Intelligenz, iMinds,
 * Institut für Angewandte Informatik e. V. an der Universität Leipzig,
 * Istituto Superiore Mario Boella, Tilde, Vistatec, WRIPL (http://freme-project.eu)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.freme.common;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import eu.freme.common.conversion.etranslate.TranslationConversionService;
import eu.freme.common.conversion.etranslate.TranslationConversionServiceImpl;
import eu.freme.common.conversion.rdf.JenaRDFConversionService;
import eu.freme.common.conversion.rdf.RDFConversionService;

/**
 * @author Jan Nehring - jan.nehring@dfki.de
 */
@SpringBootApplication
public class FREMECommonConfig {

	@Bean
	public RDFConversionService getRDFConversionService() {
		return new JenaRDFConversionService();
	}

	@Bean
	public TranslationConversionService getTranslationConversionService() {
		return new TranslationConversionServiceImpl();
	}
}
