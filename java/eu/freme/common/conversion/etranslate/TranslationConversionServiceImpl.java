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
package eu.freme.common.conversion.etranslate;

import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import eu.freme.common.conversion.rdf.RDFConstants;

/**
 * @author Jan Nehring - jan.nehring@dfki.de
 */
public class TranslationConversionServiceImpl implements
		TranslationConversionService {

	@Override
	public Resource addTranslation(String translation, Resource source,
			String targetLanguage) {

		Model model = source.getModel();

		if (!model.getNsPrefixMap().containsValue(RDFConstants.itsrdfPrefix)) {
			model.setNsPrefix("itsrdf", RDFConstants.itsrdfPrefix);
		}

		Literal literal = model.createLiteral(translation, targetLanguage);
		source.addLiteral(
				model.getProperty(RDFConstants.itsrdfPrefix + "target"),
				literal);
		return source;
	}

	@Override
	public Resource extractTextToTranslate(Model model) {
		Property isString = model.getProperty(RDFConstants.nifPrefix
				+ "isString");
		StmtIterator itr = model.listStatements((Resource) null, isString,
				(String) null);
		while (itr.hasNext()) {
			Statement st = itr.next();
			if (st.getObject().isLiteral()) {
				return st.getSubject();
			}
		}
		
		return null;
	}
}
