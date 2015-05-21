package eu.freme.conversion.etranslate;

import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

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

	@Override
	public Resource extractTextToTranslate(Model model) {
		Property isString = model.getProperty(RDFConstants.nifPrefix
				+ "isString");
		StmtIterator itr = model.listStatements((Resource) null, isString,
				(String) null);
		while (itr.hasNext()) {
			Statement st = itr.next();
			if (st.getObject().isLiteral()) {
				return st.getSubject();
			}
		}
		
		return null;
	}
}
