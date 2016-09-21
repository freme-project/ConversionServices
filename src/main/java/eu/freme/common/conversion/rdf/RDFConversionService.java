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

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;

/**
 * @author Jan Nehring - jan.nehring@dfki.de
 */
public interface RDFConversionService {

	/**
	 * Convert a plaintext string to JsonLd / NIF. The nif:isString literal will
	 * be annotated with given language when language!=null is submitted.
	 * 
	 * @param model
	 *            The model to enrich
	 * @param plaintext the used plaintext input
	 * @param language
	 *            set to null if no language is attached to isString
	 * @param prefix
	 *            defines the uri that for the new resource
	 * @return the newly generated resource
	 */

	@Deprecated
	public Resource plaintextToRDF(Model model, String plaintext,
			String language, String prefix);
	
	public Resource plaintextToRDF(Model model, String plaintext,
			String language, String prefix, String nifVersion);

	/**
	 * Serialize given model as NIF / Turtle
	 *
	 * @param model the jena rdf model to serialize
	 * @return the serialization
	 */
	public String serializeRDF(Model model, String format)
			throws Exception;

	/**
	 * @deprecated use serializeRDF(Model model, String format) instead
	 */
	@Deprecated
	public String serializeRDF(Model model, RDFConstants.RDFSerialization format)
			throws Exception;

	/**
	 * Create model from rdf string in given format
	 *
	 * @param rdf
	 *            rdf data in given format
	 * @param format
	 *            format identifier
	 * @return the deserialized jena rdf model
	 * @throws Exception
	 */
	public Model unserializeRDF(String rdf, String format)
			throws Exception;

	/**
	 * @deprecated use unserializeRDF(String rdf, String format) instead
	 */
	@Deprecated
	public Model unserializeRDF(String rdf, RDFConstants.RDFSerialization format)
			throws Exception;



	/**
	 * Extracts plaintext for enrichment. It looks for a resource that is a
	 * nif:Context and extracts returns of the nif:isString property. When there
	 * is more then one of these literals then it returns an arbitrary literal.
	 * 
	 * @param model the jena rdf model to extract the statement from
	 * @return the rdf statement containing the first plain text
	 * @throws Exception
	 */
	public Statement extractFirstPlaintext(Model model) throws Exception;
}
