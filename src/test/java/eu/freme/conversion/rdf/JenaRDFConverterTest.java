package eu.freme.conversion.rdf;

import org.junit.Test;

import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import eu.freme.conversion.rdf.RDFConversionService.RDFSerialization;
import static org.junit.Assert.*;

public class JenaRDFConverterTest {

	private final String nifPrefix = "http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#";
	private final String itsrdfPrefix = "http://www.w3.org/2005/11/its/rdf#";
	
	@Test
	public void testPlaintextToRDF() {

		JenaRDFConversionService converter = new JenaRDFConversionService();

		String plaintext = "hello world";
		String language = "en";
		Model model = ModelFactory.createDefaultModel();
		converter.plaintextToRDF(model, plaintext, language);

		assertTrue(countStatements(model.listStatements()) == 6);

		Property isString = model.createProperty(nifPrefix + "isString");
		assertTrue(countStatements(model.listStatements((Resource) null,
				isString, (RDFNode) null)) == 1);

		Property beginIndex = model.createProperty(nifPrefix + "beginIndex");
		assertTrue(countStatements(model.listStatements((Resource) null,
				beginIndex, (RDFNode) null)) == 1);

		Property endIndex = model.createProperty(nifPrefix + "endIndex");
		assertTrue(countStatements(model.listStatements((Resource) null,
				endIndex, (RDFNode) null)) == 1);

		model = ModelFactory.createDefaultModel();
		Resource res = converter.plaintextToRDF(model, plaintext, null);

		assertTrue(countStatements(model.listStatements((Resource) null,
				isString, (RDFNode) null)) == 1);
		assertTrue(countStatements(model.listStatements((Resource) null,
				beginIndex, (RDFNode) null)) == 1);
		assertTrue(countStatements(model.listStatements((Resource) null,
				endIndex, (RDFNode) null)) == 1);

		assertTrue(res.getProperty(isString).getLiteral().getLanguage().trim()
				.length() == 0);

	}

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
		Resource translationResource = converter.addTranslation(translation,
				resource, targetLang);
		
		Property itsrdfTarget = model.getProperty(itsrdfPrefix + "target");
		Literal lit = translationResource.getProperty(itsrdfTarget).getLiteral();
		assertTrue( lit.getString().equals( translation ));
		assertTrue( lit.getLanguage().equals(targetLang));	
	}
	
	@Test
	public void testSerialization(){
		JenaRDFConversionService converter = new JenaRDFConversionService();
		Model model = ModelFactory.createDefaultModel();

		converter.plaintextToRDF(model, "test", "en");
		String str = converter.serializeRDF(model, RDFSerialization.TURTLE);
		assertTrue(str.length() > 0);

		str = converter.serializeRDF(model, RDFSerialization.JSON_LD);
		assertTrue(str.length() > 0);
	}

	private int countStatements(StmtIterator itr) {
		int count = 0;
		while (itr.hasNext()) {
			count++;
			itr.next();
		}
		return count;
	}
}
