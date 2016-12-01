package eu.freme.common.conversion.rdf;

import org.springframework.stereotype.Component;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;


/**
 * 
 * @author britta (britta.grusdt@dfki.de)
 *
 */
@Component
public class NIF21Helper {
	
	/**
	 * 
	 * @param nifModel: response Model from an FREME e-service
	 * @param nifVersion 
	 * 
	 * adds the property conformsTo to the NIF document when the nifVersion
	 * is 2.1.
	 */
	public static void addConformsTo(Model nifModel, String nifVersion){
		
		if(nifVersion.equals("2.0")){
			return;
		}
		
		//check if <http://purl.org/dc/terms/conformsTo> already exists 
		Property conformsTo = ResourceFactory.createProperty("http://purl.org/dc/terms/", "conformsTo");
		StmtIterator nif21Resource = nifModel.listStatements(null, conformsTo," http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core/2.1");
		
		if(!nif21Resource.hasNext()){
			//add conformsTo since it does not yet exist

			//get context
//	        ResIterator iterEntities = nifModel.listSubjectsWithProperty(RDF.type,"http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#Context");
	        StmtIterator iter = nifModel.listStatements(null, RDF.type,
	        		nifModel.getResource(RDFConstants.nifPrefix +"Context"));
	        
	        //TODO  always just 1 context resource ?! 
	       Statement st = iter.nextStatement();
	       Resource context = st.getSubject();
	        
	       
	       //TODO always with freme-project.eu ? 
	       Resource collectionResource = nifModel.createResource("http://freme-project.eu/#collection");
	       Resource r  = nifModel.createResource(RDFConstants.nifPrefix + "ContextCollection");
	       nifModel.add(collectionResource, RDF.type, r);
	       
	       Property hasContextProperty = nifModel.createProperty(RDFConstants.nifPrefix + "hasContext");
	       nifModel.add(collectionResource, hasContextProperty, context);
	       
	       nifModel.add(collectionResource, conformsTo,"http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core/2.1");
			
		}
	}
}
