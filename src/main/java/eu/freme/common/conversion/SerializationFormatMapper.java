package eu.freme.common.conversion;

import eu.freme.common.conversion.rdf.RDFConstants;
import eu.freme.common.exception.InternalServerErrorException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * This class should be used to normalize serialization formats.
 * Every class working with serialization formats should autowire
 * this to map formats to common values and to add specific format
 * mappings, if necessary.
 */
@Component
public class SerializationFormatMapper extends HashMap<String, String> {

    public static final String PLAINTEXT = "text/plain";
    public static final String JSON = "application/json";

    public SerializationFormatMapper(){
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


    // protect existing entries
    @Override
    public String put(String key, String value) throws InternalServerErrorException{
        if(containsKey(key) && !get(key).equals(value)){
            throw new InternalServerErrorException(
                    "The SerializationFormatMapper contains already the value='"
                            +get(key)+"' for the key='"+key+"', it can not be " +
                            "replaced by the value='"+value+"'. Existing values " +
                            "can not be overwritten.");
        }
        return super.put(key, value);
    }

    @Override
    public void putAll(Map<? extends String, ? extends String> m) throws InternalServerErrorException{
        for(String key: m.keySet()){
           put(key, m.get(key));
        }
    }
}
