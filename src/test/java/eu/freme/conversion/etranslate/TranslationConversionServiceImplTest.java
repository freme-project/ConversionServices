package eu.freme.conversion.etranslate;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;

import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

import eu.freme.conversion.rdf.JenaRDFConversionService;
import eu.freme.conversion.rdf.RDFConstants;

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
		Model model = converter.unserializeRDF(rdf,
				RDFConstants.RDFSerialization.TURTLE);

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
		model = converter.unserializeRDF(rdf,
				RDFConstants.RDFSerialization.TURTLE);
		resource = translationConversionService
				.extractTextToTranslate(model);
		assertTrue(resource == null);
	}
}
