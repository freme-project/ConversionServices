package eu.freme.conversion.rdf;

import java.io.StringReader;
import java.io.StringWriter;

import com.hp.hpl.jena.datatypes.RDFDatatype;
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

public class JenaRDFConversionService implements RDFConversionService {

	final String nifPrefix = "http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#";
	final String itsrdfPrefix = "http://www.w3.org/2005/11/its/rdf#";
	final String xsdPrefix = "http://www.w3.org/2001/XMLSchema#";

	public Resource plaintextToRDF(Model model, String plaintext,
			String language) {

		model.setNsPrefix("nif", nifPrefix);
		model.setNsPrefix("xsd", "http://www.w3.org/2001/XMLSchema#");

		String uri = "http://freme-project.eu/resource/tmp#char=0,"
				+ plaintext.length();
		Resource resource = model.createResource(uri);

		Property type = model
				.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
		resource.addProperty(type, model.createResource(nifPrefix + "String"));
		resource.addProperty(type, model.createResource(nifPrefix + "Context"));
		resource.addProperty(type,
				model.createResource(nifPrefix + "RFC5147String"));

		if (language == null) {
			resource.addProperty(model.createProperty(nifPrefix + "isString"),
					model.createLiteral(plaintext));
		} else {
			resource.addProperty(model.createProperty(nifPrefix + "isString"),
					model.createLiteral(plaintext, language));
		}

		Literal beginIndex = model.createTypedLiteral(new Integer(0),
				XSDDatatype.XSDnonNegativeInteger);
		resource.addProperty(model.createProperty(nifPrefix + "beginIndex"),
				beginIndex);
		Literal endIndex = model.createTypedLiteral(
				new Integer(plaintext.length()),
				XSDDatatype.XSDnonNegativeInteger);
		resource.addProperty(model.createProperty(nifPrefix + "endIndex"),
				endIndex);

		return resource;
	}

	public Resource addTranslation(String translation, Resource source,
			String targetLanguage) {

		Model model = source.getModel();

		if (!model.getNsPrefixMap().containsValue(itsrdfPrefix)) {
			model.setNsPrefix("itsrdf", itsrdfPrefix);
		}

		Literal literal = model.createLiteral(translation, targetLanguage);
		source.addLiteral(model.getProperty(itsrdfPrefix + "target"),
				literal);
		return source;
	}

	@Override
	public String serializeRDF(Model model, RDFSerialization format) {
		
		String jenaIdentifier = null;
		if( format == RDFSerialization.TURTLE ){
			jenaIdentifier = "TTL";
		} else if( format == RDFSerialization.JSON_LD ){
			jenaIdentifier = "JSON-LD";
		} else{
			throw new RuntimeException( "unsupported format: " + format );
		}
		
		StringWriter writer = new StringWriter();
		model.write(writer, jenaIdentifier);
		return writer.toString();
	}

}
