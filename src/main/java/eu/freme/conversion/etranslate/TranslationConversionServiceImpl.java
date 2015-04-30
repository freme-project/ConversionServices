package eu.freme.conversion.etranslate;

import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;

import eu.freme.conversion.rdf.RDFConstants;

public class TranslationConversionServiceImpl implements
		TranslationConversionService {

	public Resource addTranslation(String translation, Resource source,
			String targetLanguage) {

		Model model = source.getModel();

		if (!model.getNsPrefixMap().containsValue(RDFConstants.itsrdfPrefix)) {
			model.setNsPrefix("itsrdf", RDFConstants.itsrdfPrefix);
		}

		Literal literal = model.createLiteral(translation, targetLanguage);
		source.addLiteral(
				model.getProperty(RDFConstants.itsrdfPrefix + "target"),
				literal);
		return source;
	}
}
