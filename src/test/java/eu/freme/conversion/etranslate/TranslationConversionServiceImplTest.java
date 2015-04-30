package eu.freme.conversion.etranslate;

import static org.junit.Assert.*;

import org.junit.Test;

import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

import eu.freme.conversion.rdf.JenaRDFConversionService;
import eu.freme.conversion.rdf.RDFConstants;

public class TranslationConversionServiceImplTest {

	@Test
	public void testAddTranslation() {
		String plaintext = "hallo welt";
		String sourceLang = "de";
		String translation = "hello world";
		String targetLang = "en";

		JenaRDFConversionService converter = new JenaRDFConversionService();
		Model model = ModelFactory.createDefaultModel();
		Resource resource = converter.plaintextToRDF(model, plaintext,
				sourceLang);

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
}
