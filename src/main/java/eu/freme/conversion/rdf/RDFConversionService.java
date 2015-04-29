package eu.freme.conversion.rdf;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;

public interface RDFConversionService {
	
	/**
	 * Convert a plaintext string to JsonLd / NIF. The nif:isString literal will
	 * be annotated with given language when language!=null is submitted.
	 * 
	 * @param model
	 *            The model to enrich
	 * @param plaintext
	 * @param language
	 * @return the newly generated resource
	 */
	public Resource plaintextToRDF(Model model, String plaintext,
			String language);

	/**
	 * Enrich an existing RDF model with translation information.
	 * 
	 * @param translation
	 *            The translated string
	 * @param source
	 *            The resource that is enriched
	 * @param targetLanguage
	 *            The target language identifier (e.g. "en" or "de")
	 * @param model
	 *            The model that is going to store the information. This model
	 *            is going to be changed through this call.
	 * @return the newly generated resource
	 */
	public Resource addTranslation(String translation, Resource source,
			String targetLanguage);
	
	enum RDFSerialization{
		TURTLE,
		JSON_LD
	}
	
	/**
	 * Serialize given model as NIF / Turtle
	 * 
	 * @param model
	 * @return
	 */
	public String serializeRDF(Model model, RDFSerialization format);
}
