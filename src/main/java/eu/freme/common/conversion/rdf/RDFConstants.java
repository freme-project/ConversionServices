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

import java.util.UnknownFormatConversionException;

/**
 * @author Jan Nehring - jan.nehring@dfki.de
 */
public class RDFConstants {

	public static final String nifPrefix = "http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#";
	public static final String itsrdfPrefix = "http://www.w3.org/2005/11/its/rdf#";
	public static final String xsdPrefix = "http://www.w3.org/2001/XMLSchema#";

	public enum RDFSerialization {
		TURTLE("text/turtle"),
		JSON_LD("application/ld+json"),
		PLAINTEXT("text/plain"),
		RDF_XML("application/rdf+xml"),
		N3("text/n3"),
		N_TRIPLES("application/n-triples"),
		JSON("application/json"),
		HTML("text/html"),
		CSV("text/comma-separated-values"),
		XML("text/xml");

		private final String contentType;

		RDFSerialization(String contentType) {
			this.contentType = contentType;
		}

		public String contentType() {
			return contentType;
		}

		/**
		 * Given a textual content type, return its RDFSerialization object.
		 * @param contentType	The content type, in textual format.
		 * @return				The corresponding RDFSerialization object, or {@code null} if nothing found
		 */
		public static RDFSerialization fromValue(final String contentType) {
			String normalizedContentType = contentType.toLowerCase();

			// chop off everything beginning from ';'. An example is "text/turtle; charset=UTF-8"
			int indexOfSemicolon = normalizedContentType.indexOf(';');
			if (indexOfSemicolon >= 0) {
				normalizedContentType = normalizedContentType.substring(0, indexOfSemicolon);
			}

			// now find the matching value
			for (RDFSerialization rdfSerialization : RDFSerialization.values()) {
				if (rdfSerialization.contentType().equals(normalizedContentType)) {
					return rdfSerialization;
				}
			}
			return null;
		}

		@Deprecated
		public String getMimeType(){
			switch(this) {
				case JSON:
					return "application/json";
				case TURTLE:
					return  "text/turtle";
				case JSON_LD:
					return  "application/ld+json";
				case RDF_XML:
					return  "application/rdf+xml";
				case N_TRIPLES:
					return  "application/n-triples";
				case N3:
					return  "text/n3";
				case PLAINTEXT:
					return  "text/plain";
				case HTML:
					return  "text/html";
			}
			throw new UnknownFormatConversionException("unkown RDFSerialization: "+ this);
		}
	}
}
