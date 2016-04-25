package eu.freme.common.rest;

import eu.freme.common.conversion.rdf.RDFConstants;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * Created by Arne Binder (arne.b.binder@gmail.com) on 25.04.2016.
 */
@Component
public class MimeTypeMapper extends HashMap<String, String> {

    public static final String PLAINTEXT = "text/plain";
    public static final String JSON = "application/json";

    public MimeTypeMapper(){
        super();

        // add plain text
        put(PLAINTEXT, PLAINTEXT);
        put("text", PLAINTEXT);

        // add json
        put(JSON, JSON);
        put("json", JSON);

        // add RDF types
        put(RDFConstants.TURTLE, RDFConstants.TURTLE);
        put("application/x-turtle", RDFConstants.TURTLE);
        put("turtle", RDFConstants.TURTLE);

        put(RDFConstants.JSON_LD, RDFConstants.JSON_LD);
        put("application/json+ld", RDFConstants.JSON_LD);
        put("json-ld", RDFConstants.JSON_LD);

        put(RDFConstants.N_TRIPLES, RDFConstants.N_TRIPLES);
        put("n-triples", RDFConstants.N_TRIPLES);

        put(RDFConstants.RDF_XML, RDFConstants.RDF_XML);
        put("rdf-xml", RDFConstants.RDF_XML);

        put(RDFConstants.N3, RDFConstants.N3);
        put("n3", RDFConstants.N3);
    }
}
