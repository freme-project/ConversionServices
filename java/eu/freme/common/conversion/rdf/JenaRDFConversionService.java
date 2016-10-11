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
package eu.freme.common.conversion.rdf;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;

import eu.freme.common.exception.NIFVersionNotSupportedException;
import eu.freme.common.exception.UnsupportedRDFSerializationException;

import static eu.freme.common.conversion.rdf.RDFConstants.*;

/**
 * @author Jan Nehring - jan.nehring@dfki.de
 */
public class JenaRDFConversionService implements RDFConversionService {

	public final static String JENA_TURTLE = "TTL";
	public final static String JENA_JSON_LD = "JSON-LD";
	public final static String JENA_N_TRIPLES = "N-TRIPLES";
	public final static String JENA_N3 = "N3";
	public final static String JENA_RDF_XML = "RDF/XML";

	// map from our rdf types to jena format
	private static Map<String, String> rdfTypeMapping = constructJenaTypeMappings();

	private static Map<String, String> constructJenaTypeMappings(){
		HashMap<String, String> result = new HashMap<>();
		result.put(RDFConstants.TURTLE, JENA_TURTLE);
		result.put(RDFConstants.JSON_LD, JENA_JSON_LD);
		result.put(RDFConstants.N_TRIPLES, JENA_N_TRIPLES);
		result.put(RDFConstants.N3, JENA_N3);
		result.put(RDFConstants.RDF_XML, JENA_RDF_XML);
		return result;
	}

	/**
	 * Convert plaintext to NIF 2.1
	 * 
	 * @param model
	 * @param plaintext
	 * @param language
	 * @param prefix
	 * @return
	 */
	private Resource plaintextToNIF2_1(Model model, String plaintext,
			String language, String prefix) {
		model.setNsPrefix("nif", nifPrefix);
		model.setNsPrefix("xsd", xsdPrefix);

		String uri = prefix;
		if (!uri.contains(NIF20_OFFSET) && !uri.contains(NIF21_OFFSET)) {
			uri += NIF21_OFFSET+"0_" + plaintext.length();
		}
		Resource resource = model.createResource(uri);

		Property type = model
				.createProperty(typePrefix);
		resource.addProperty(type,
				model.createResource(nifPrefix + NIF_CONTEXT_TYPE));
		resource.addProperty(
				type,
				model.createResource(nifPrefix
						+ NIF21_STRINGS_IDENTIFIER));

		if (language == null) {
			resource.addProperty(
					model.createProperty(nifPrefix + IS_STRING),
					model.createTypedLiteral(plaintext, XSDDatatype.XSDstring));
		} else {
			resource.addProperty(
					model.createProperty(nifPrefix + IS_STRING),
					model.createLiteral(plaintext, language));
		}

		Literal beginIndex = model.createTypedLiteral(new Integer(0),
				XSDDatatype.XSDnonNegativeInteger);
		resource.addProperty(
				model.createProperty(nifPrefix + BEGIN_INDEX),
				beginIndex);
		Literal endIndex = model.createTypedLiteral(
				new Integer(plaintext.length()),
				XSDDatatype.XSDnonNegativeInteger);
		resource.addProperty(
				model.createProperty(nifPrefix + END_INDEX),
				endIndex);

		return resource;
	}

	/**
	 * Convert plaintext to NIF 2.0
	 * 
	 * @param model
	 * @param plaintext
	 * @param language
	 * @param prefix
	 * @return
	 */
	private Resource plaintextToNIF2_0(Model model, String plaintext,
			String language, String prefix) {

		model.setNsPrefix("nif", nifPrefix);
		model.setNsPrefix("xsd", xsdPrefix);

		String uri = prefix;
		if (!uri.contains(NIF20_OFFSET)) {
			uri += NIF20_OFFSET+"0," + plaintext.length();
		}
		Resource resource = model.createResource(uri);

		Property type = model
				.createProperty(typePrefix);
		resource.addProperty(type,
				model.createResource(nifPrefix + NIF_STRING_TYPE));
		resource.addProperty(type,
				model.createResource(nifPrefix + NIF_CONTEXT_TYPE));
		resource.addProperty(type,
				model.createResource(nifPrefix + NIF20_STRINGS_IDENTIFIER));

		if (language == null) {
			resource.addProperty(
					model.createProperty(nifPrefix + IS_STRING),
					model.createLiteral(plaintext));
		} else {
			resource.addProperty(
					model.createProperty(nifPrefix + IS_STRING),
					model.createLiteral(plaintext, language));
		}

		Literal beginIndex = model.createTypedLiteral(new Integer(0),
				XSDDatatype.XSDnonNegativeInteger);
		resource.addProperty(
				model.createProperty(nifPrefix + BEGIN_INDEX),
				beginIndex);
		Literal endIndex = model.createTypedLiteral(
				new Integer(plaintext.length()),
				XSDDatatype.XSDnonNegativeInteger);
		resource.addProperty(
				model.createProperty(nifPrefix + END_INDEX),
				endIndex);

		return resource;
	}

	/**
	 * Convert plaintext to NIF 2.0. You should better use the other
	 * implementation of this function that lets you specify the NIF version.
	 * @deprecated use {@link #plaintextToRDF(Model model, String plaintext,
	 * 		String language, String prefix, String nifVersion)} instead
	 */
	@Deprecated
	@Override
	public Resource plaintextToRDF(Model model, String plaintext,
			String language, String prefix) {
		return plaintextToNIF2_0(model, plaintext, language, prefix);
	}

	/**
	 * Convert Jena Model to String
	 */
	@Override
	public String serializeRDF(Model model, String format) throws Exception {

		String jenaIdentifier = getJenaType(format);
		if (jenaIdentifier == null) {
			throw new UnsupportedRDFSerializationException("unsupported format: " + format);
		}

		StringWriter writer = new StringWriter();
		model.write(writer, jenaIdentifier);
		writer.close();
		return writer.toString();
	}

	/**
	 * @deprecated use {@link #serializeRDF(Model, String)} instead
     */
	@Deprecated
	@Override
	public String serializeRDF(Model model, RDFConstants.RDFSerialization format)
			throws Exception {
		return serializeRDF(model, format.contentType());
	}

	/**
	 * Convert String to Jena Model
	 */
	@Override
	public Model unserializeRDF(String rdf, String format) throws Exception {
		String jenaIdentifier = getJenaType(format);
		if (jenaIdentifier == null) {
			throw new UnsupportedRDFSerializationException("unsupported format: " + format);
		}
		Model model = ModelFactory.createDefaultModel();
		StringReader reader = new StringReader(rdf);
		model.read(reader, null, jenaIdentifier);

		return model;
	}

	/**
	 * @deprecated use {@link #unserializeRDF(String rdf, String format)} instead
     */
	@Deprecated
	@Override
	public Model unserializeRDF(String rdf, RDFConstants.RDFSerialization format)
			throws Exception {

		return unserializeRDF(rdf, format.contentType());
	}

	/**
	 * @deprecated use {@link #getJenaType(String type)} instead
	 */
	@Deprecated
	public static String getJenaType(RDFConstants.RDFSerialization type) {
		return rdfTypeMapping.get(type.contentType());
	}

	public static String getJenaType(String type) {
		return rdfTypeMapping.get(type);
	}

	/**
	 * Find one context in an RDF document with an isString property and return
	 * this statement.
	 */
	@Override
	public Statement extractFirstPlaintext(Model model) throws Exception {
		Resource context = model
				.getResource(nifPrefix+NIF_CONTEXT_TYPE);
		Property isString = model
				.getProperty(nifPrefix+IS_STRING);
		StmtIterator iter = model.listStatements(null, RDF.type, context);
		while (iter.hasNext()) {
			Resource contextRes = iter.nextStatement().getSubject();
			Statement isStringStm = contextRes.getProperty(isString);
			if (isStringStm != null) {
				return isStringStm;
			}
		}
		return null;
	}

	/**
	 * Convert plaintext to NIF, using one of the RDFConstants.nifVersion
	 * variables.
	 */
	@Override
	public Resource plaintextToRDF(Model model, String plaintext,
			String language, String prefix, String nifVersion) {
		if (nifVersion == null || nifVersion.equals(RDFConstants.nifVersion2_0)) {
			return plaintextToNIF2_0(model, plaintext, language, prefix);
		} else if (nifVersion.equals(RDFConstants.nifVersion2_1)) {
			return plaintextToNIF2_1(model, plaintext, language, prefix);
		} else {
			throw new NIFVersionNotSupportedException("NIF version \""
					+ nifVersion + "\" is not supported");
		}
	}

}
