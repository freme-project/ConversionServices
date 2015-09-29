/**
 * Copyright (C) 2015 Agro-Know, Deutsches Forschungszentrum für Künstliche Intelligenz, iMinds,
 * 							Institut für Angewandte Informatik e. V. an der Universität Leipzig,
 * 							Istituto Superiore Mario Boella, Tilde, Vistatec, WRIPL (http://freme-project.eu)
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
package eu.freme.conversion.rdf;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;

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
	 * @param plaintext
	 * @param language
	 *            set to null if no language is attached to isString
	 * @param prefix defines the uri that for the new resource
	 * @return the newly generated resource
	 */
	public Resource plaintextToRDF(Model model, String plaintext,
			String language, String prefix);

	/**
	 * Serialize given model as NIF / Turtle
	 * 
	 * @param model
	 * @return
	 */
	public String serializeRDF(Model model, RDFConstants.RDFSerialization format)
			throws Exception;

	/**
	 * Create model from rdf string in given format
	 * 
	 * @param rdf
	 *            rdf data in given format
	 * @param format
	 *            format identifier
	 * @return
	 * @throws Exception
	 */
	public Model unserializeRDF(String rdf, RDFConstants.RDFSerialization format)
			throws Exception;
}
