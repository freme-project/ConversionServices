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
package eu.freme.common.conversion.etranslate;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;

import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

import eu.freme.common.conversion.rdf.JenaRDFConversionService;
import eu.freme.common.conversion.rdf.RDFConstants;

/**
 * @author Jan Nehring - jan.nehring@dfki.de
 */
public class TranslationConversionServiceImplTest {

	@Test
	public void testAddTranslation() {
		String plaintext = "hallo welt";
		String sourceLang = "de";
		String translation = "hello world";
		String targetLang = "en";
		String prefix = "http://freme-project.eu/";

		JenaRDFConversionService converter = new JenaRDFConversionService();
		Model model = ModelFactory.createDefaultModel();
		Resource resource = converter.plaintextToRDF(model, plaintext,
				sourceLang, prefix);

		TranslationConversionServiceImpl translateConversionService = new TranslationConversionServiceImpl();

		Resource translationResource = translateConversionService
				.addTranslation(translation, resource, targetLang);

		Property itsrdfTarget = model.getProperty(RDFConstants.itsrdfPrefix
				+ "target");
		Literal lit = translationResource.getProperty(itsrdfTarget)
				.getLiteral();
		assertTrue(lit.getString().equals(translation));
		assertTrue(lit.getLanguage().equals(targetLang));
	}

	private String readFile(String file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		StringBuilder bldr = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			bldr.append(line);
			bldr.append("\n");
		}
		reader.close();
		return bldr.toString();
	}

	@Test
	public void testExtractTextToTranslate() throws Exception {
		// model that contains a string to translate
		JenaRDFConversionService converter = new JenaRDFConversionService();
		String rdf = readFile("src/test/resources/rdftest/test.turtle");
		Model model = converter.unserializeRDF(rdf, RDFConstants.TURTLE);

		TranslationConversionServiceImpl translationConversionService = new TranslationConversionServiceImpl();
		Resource resource = translationConversionService
				.extractTextToTranslate(model);
		Property isString = model.getProperty(RDFConstants.nifPrefix
				+ "isString");
		String plaintext = resource.getProperty(isString).getObject().asLiteral().getString();
		assertTrue(plaintext != null);
		assertTrue(plaintext.length() > 0);

		// model that does not contain a string to translate
		rdf = readFile("src/test/resources/rdftest/test2.turtle");
		model = converter.unserializeRDF(rdf, RDFConstants.TURTLE);
		resource = translationConversionService
				.extractTextToTranslate(model);
		assertTrue(resource == null);
	}
}
