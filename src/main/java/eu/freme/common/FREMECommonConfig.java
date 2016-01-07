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

import eu.freme.common.persistence.tools.AccessLevelHelper;
import eu.freme.common.rest.NIFParameterFactory;
import eu.freme.common.security.voter.OwnedResourceAccessDecisionVoter;
import eu.freme.common.security.voter.UserAccessDecisionVoter;
import eu.freme.common.starter.FREMEStarter;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

import eu.freme.common.conversion.etranslate.TranslationConversionService;
import eu.freme.common.conversion.etranslate.TranslationConversionServiceImpl;
import eu.freme.common.conversion.rdf.JenaRDFConversionService;
import eu.freme.common.conversion.rdf.RDFConversionService;
import eu.freme.common.conversion.rdf.RDFSerializationFormats;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;

import java.util.ArrayList;

/**
 * @author Jan Nehring - jan.nehring@dfki.de
 */
//@EntityScan("eu.freme.common.persistence.model")
//@ComponentScan(basePackages={"eu.freme.common"})
//@Import(SecurityConfig.class)
//@EnableAutoConfiguration
//@EnableJpaRepositories(basePackages={"eu.freme.common.persistence.repository"})
@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackageClasses=FREMECommonConfig.class, excludeFilters=@Filter(type=FilterType.ASSIGNABLE_TYPE, value=FREMEStarter.class))

public class FREMECommonConfig {

    @Bean
    public RDFSerializationFormats rdfFormats(){
    	return new RDFSerializationFormats();
    }
    
    @Bean
    public NIFParameterFactory getNifParameterFactory(){
    	return new NIFParameterFactory();
    }
    
	@Bean
	public RDFConversionService getRDFConversionService() {
		return new JenaRDFConversionService();
	}

	@Bean
	public TranslationConversionService getTranslationConversionService() {
		return new TranslationConversionServiceImpl();
	}

	@Bean
	public AffirmativeBased defaultAccessDecisionManager() {
		@SuppressWarnings("rawtypes")
		ArrayList<AccessDecisionVoter> list = new ArrayList<AccessDecisionVoter>();
		//list.add(new TemplateAccessDecisionVoter());
		list.add(new UserAccessDecisionVoter());
		list.add(new OwnedResourceAccessDecisionVoter());
		AffirmativeBased ab = new AffirmativeBased(list);
		return ab;
	}

	@Bean
	public AccessLevelHelper accessLevelHelper() {
		return new AccessLevelHelper();
	}
}
