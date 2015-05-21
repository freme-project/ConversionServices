package eu.freme.conversion.etranslate;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;

public interface TranslationConversionService {

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

	/**
	 * Returns the first Reosurce in a model that has a property
	 * nif:isString. Returns null if no such literal exists in the model.
	 * 
	 * @param resource
	 * @param sourceLanguage
	 * @return
	 */
	public Resource extractTextToTranslate(Model resource);
}
