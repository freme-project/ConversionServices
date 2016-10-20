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
package eu.freme.common.conversion.rdf;

import java.util.HashMap;

/**
 * Defines the RDFSerializationFormats accepted by the REST endpoints.
 *
 * @author Jan Nehring - jan.nehring@dfki.de
 * @deprecated use {@link eu.freme.common.conversion.SerializationFormatMapper} instead
 */
@Deprecated
@SuppressWarnings({"serial", "unused"})
public class RDFSerializationFormats extends
		HashMap<String, RDFConstants.RDFSerialization> {

	public RDFSerializationFormats() {
		super();
		put("text/turtle", RDFConstants.RDFSerialization.TURTLE);
		put("application/x-turtle", RDFConstants.RDFSerialization.TURTLE);
		put("turtle", RDFConstants.RDFSerialization.TURTLE);

		put("application/json+ld", RDFConstants.RDFSerialization.JSON_LD);
		put("application/ld+json", RDFConstants.RDFSerialization.JSON_LD);
		put("json-ld", RDFConstants.RDFSerialization.JSON_LD);

		put("application/n-triples", RDFConstants.RDFSerialization.N_TRIPLES);
		put("n-triples", RDFConstants.RDFSerialization.N_TRIPLES);

		put("text/plain", RDFConstants.RDFSerialization.PLAINTEXT);
		put("text", RDFConstants.RDFSerialization.PLAINTEXT);

		put("application/rdf+xml", RDFConstants.RDFSerialization.RDF_XML);
		put("rdf-xml", RDFConstants.RDFSerialization.RDF_XML);

		put("text/n3", RDFConstants.RDFSerialization.N3);
		put("n3", RDFConstants.RDFSerialization.N3);

		put("json", RDFConstants.RDFSerialization.JSON);
		put("application/json", RDFConstants.RDFSerialization.JSON);

		put("html", RDFConstants.RDFSerialization.HTML);
		put("text/html", RDFConstants.RDFSerialization.HTML);

		put("csv", RDFConstants.RDFSerialization.CSV);
		put("text/comma-separated-values", RDFConstants.RDFSerialization.CSV);

		put("xml", RDFConstants.RDFSerialization.XML);
		put("text/xml", RDFConstants.RDFSerialization.XML);
	}
}
