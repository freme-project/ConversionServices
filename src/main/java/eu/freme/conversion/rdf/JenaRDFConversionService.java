package eu.freme.conversion.rdf;

public class JenaRDFConversionService implements RDFConversionService{

	public String plaintextToNifJsonLd(String plaintext) {
		return plaintext.toUpperCase();
	}
}
