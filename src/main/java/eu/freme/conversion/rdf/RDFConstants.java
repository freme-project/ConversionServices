package eu.freme.conversion.rdf;

public class RDFConstants {

	public static final String nifPrefix = "http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#";
	public static final String itsrdfPrefix = "http://www.w3.org/2005/11/its/rdf#";
	public static final String xsdPrefix = "http://www.w3.org/2001/XMLSchema#";

	public enum RDFSerialization {
		TURTLE, JSON_LD
	}
}
