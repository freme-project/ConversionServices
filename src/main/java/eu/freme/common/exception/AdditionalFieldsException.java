/**
 * Copyright © 2015 Agro-Know, Deutsches Forschungszentrum für Künstliche Intelligenz, iMinds,
 * Institut für Angewandte Informatik e. V. an der Universität Leipzig,
 * Istituto Superiore Mario Boella, Tilde, Vistatec, WRIPL (http://freme-project.eu)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.freme.common.exception;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Arne Binder (arne.b.binder@gmail.com) on 22.08.2016.
 */
public abstract class AdditionalFieldsException extends FREMEHttpException {

    private Map<String, JSONObject> additionalFields = new HashMap<>();

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

    public Map<String, JSONObject> getAdditionalFields() {
        return additionalFields;
    }

    protected void addAdditionalField(String key, JSONObject value){
        additionalFields.put(key, value);
    }
}
