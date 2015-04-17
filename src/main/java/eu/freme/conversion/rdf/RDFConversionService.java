package eu.freme.conversion.rdf;

public interface RDFConversionService {

	/**
	 * Convert a plaintext string to JsonLd / NIF
	 * 
	 * @param plaintext
	 * @return nif encoded as json-ld
	 */
	public String plaintextToNifJsonLd( String plaintext );
}
