package eu.freme.conversion.rdf;

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
		JSON("application/json");

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
	}
}
