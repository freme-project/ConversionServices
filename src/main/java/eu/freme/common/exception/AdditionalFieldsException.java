package eu.freme.common.exception;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Arne Binder (arne.b.binder@gmail.com) on 22.08.2016.
 */
public abstract class AdditionalFieldsException extends FREMEHttpException {

    private Map<String, Object> additionalFields = new HashMap<>();

    public AdditionalFieldsException(){
        super();
    }

    public AdditionalFieldsException(String msg){
        super(msg);
    }

    public AdditionalFieldsException(String msg, HttpStatus httpStatusCode){
        super(msg);
        setHttpStatusCode(httpStatusCode);
    }

    public AdditionalFieldsException(HttpStatus httpStatusCode){
        super();
        setHttpStatusCode(httpStatusCode);
    }

    public Map<String, Object> getAdditionalFields() {
        return additionalFields;
    }

    protected void addAdditionalField(String key, Object value){
        additionalFields.put(key, value);
    }
}
