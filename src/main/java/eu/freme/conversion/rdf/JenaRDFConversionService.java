package eu.freme.conversion.rdf;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * @author Jan Nehring - jan.nehring@dfki.de
 */
public class JenaRDFConversionService implements RDFConversionService {

	// map from our rdf types to jena format
	HashMap<RDFConstants.RDFSerialization, String> rdfTypeMapping;

	public JenaRDFConversionService() {
		rdfTypeMapping = new HashMap<RDFConstants.RDFSerialization, String>();
		rdfTypeMapping.put(RDFConstants.RDFSerialization.TURTLE, "TTL");
		rdfTypeMapping.put(RDFConstants.RDFSerialization.JSON_LD, "JSON-LD");
		rdfTypeMapping.put(RDFConstants.RDFSerialization.N_TRIPLES, "N-TRIPLES");
		rdfTypeMapping.put(RDFConstants.RDFSerialization.N3, "N3");
		rdfTypeMapping.put(RDFConstants.RDFSerialization.RDF_XML, "RDF/XML");
	}

	public Resource plaintextToRDF(Model model, String plaintext,
			String language, String prefix) {

		model.setNsPrefix("nif", RDFConstants.nifPrefix);
		model.setNsPrefix("xsd", "http://www.w3.org/2001/XMLSchema#");

		String uri = prefix;
		if( !uri.contains("#char=")){
			uri += "#char=0," + plaintext.length();
		}
		Resource resource = model.createResource(uri);

		Property type = model
				.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
		resource.addProperty(type,
				model.createResource(RDFConstants.nifPrefix + "String"));
		resource.addProperty(type,
				model.createResource(RDFConstants.nifPrefix + "Context"));
		resource.addProperty(type,
				model.createResource(RDFConstants.nifPrefix + "RFC5147String"));

		if (language == null) {
			resource.addProperty(
					model.createProperty(RDFConstants.nifPrefix + "isString"),
					model.createLiteral(plaintext));
		} else {
			resource.addProperty(
					model.createProperty(RDFConstants.nifPrefix + "isString"),
					model.createLiteral(plaintext, language));
		}

		Literal beginIndex = model.createTypedLiteral(new Integer(0),
				XSDDatatype.XSDnonNegativeInteger);
		resource.addProperty(
				model.createProperty(RDFConstants.nifPrefix + "beginIndex"),
				beginIndex);
		Literal endIndex = model.createTypedLiteral(
				new Integer(plaintext.length()),
				XSDDatatype.XSDnonNegativeInteger);
		resource.addProperty(
				model.createProperty(RDFConstants.nifPrefix + "endIndex"),
				endIndex);

		return resource;
	}

	@Override
	public String serializeRDF(Model model, RDFConstants.RDFSerialization format)
			throws Exception {

		String jenaIdentifier = rdfTypeMapping.get(format);
		if (jenaIdentifier == null) {
			throw new RuntimeException("unsupported format: " + format);
		}

		StringWriter writer = new StringWriter();
		model.write(writer, jenaIdentifier);
		writer.close();
		return writer.toString();
	}

	@Override
	public Model unserializeRDF(String rdf, RDFConstants.RDFSerialization format)
			throws Exception {

		String jenaIdentifier = rdfTypeMapping.get(format);
		if (jenaIdentifier == null) {
			throw new RuntimeException("unsupported format: " + format);
		}
		Model model = ModelFactory.createDefaultModel();
		StringReader reader = new StringReader(rdf);
		model.read(reader, null, jenaIdentifier);
		
		return model;
	}

}
