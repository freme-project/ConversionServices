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
		JSON_LD("application/json+ld"),
		PLAINTEXT("text"),
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
	}
}
