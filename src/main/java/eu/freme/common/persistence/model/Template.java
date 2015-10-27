/**
 * Copyright (C) 2015 Agro-Know, Deutsches Forschungszentrum für Künstliche Intelligenz, iMinds,
 * Institut für Angewandte Informatik e. V. an der Universität Leipzig,
 * Istituto Superiore Mario Boella, Tilde, Vistatec, WRIPL (http://freme-project.eu)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.freme.common.persistence.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Strings;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.vocabulary.DCTerms;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import eu.freme.common.exception.BadRequestException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.io.IOException;

/**
 * Created by Arne Binder (arne.b.binder@gmail.com) on 01.10.2015.
 */
@Component
@Entity
@Table(name = "template")
public class Template extends OwnedResource {

    public enum Type {
        SPARQL, LDF;

        public static Type getByString(String type){
            if(Strings.isNullOrEmpty(type) || type.toLowerCase().equals("sparql"))
                return SPARQL;
            if(type.toLowerCase().equals("ldf"))
                return LDF;
            throw new BadRequestException("Wrong value for type = \""+"\". Should be \"sparql\" or \"ldf\"");
        }
    }

    @Lob
    private String endpoint;
    @Lob
    private String query;
    @Lob
    private String label;
    @Lob
    private String description;

    private Type type;

    public Template(User owner, Visibility visibility, Type type, String endpoint, String query, String label, String description) {
        super(-1, owner, visibility);
        this.endpoint = endpoint;
        this.query = query;
        this.label = label;
        this.description = description;
        this.type = type;
    }
    public Template(Visibility visibility, Type type, String endpoint, String query, String label, String description) {
        super(-1, visibility);
        this.endpoint = endpoint;
        this.query = query;
        this.label = label;
        this.description = description;
        this.type = type;
    }

    public Template(User owner, Visibility visibility, Type type, Model model){
        super(-1, owner, visibility);
        setTemplateWithModel(model);
        this.type = type;
    }

    public Template(Visibility visibility, Type type, Model model){
        super(-1, visibility);
        setTemplateWithModel(model);
        this.type = type;
    }

    public Template(JSONObject newData){
        super(-1, OwnedResource.Visibility.getByString(newData.getString("visibility")));
        update(newData);
        // set default, if key "type" was not in newData
        this.type = Type.getByString(newData.getString("endpoint"));
    }

    public Template(){super();}


    public void setTemplateWithModel(Model model){
        model.enterCriticalSection(false);
        try {
            StmtIterator iter = model.listStatements(null, RDF.type, model.getResource("http://www.freme-project.eu/ns#Template"));

            // take first instance
            if(iter.hasNext()){
                Statement templateRes = iter.nextStatement();
                Resource templRes = templateRes.getSubject();
                endpoint = templRes.getProperty(model.getProperty("http://www.freme-project.eu/ns#endpoint")).getObject().asLiteral().toString();
                query = templRes.getProperty(model.getProperty("http://www.freme-project.eu/ns#query")).getObject().asLiteral().toString();
                label = templRes.getProperty(RDFS.label).getObject().asLiteral().toString();
                description = templRes.getProperty(DCTerms.description).getObject().asLiteral().toString();
            }

        } finally {
            model.leaveCriticalSection();
        }
    }

    public void update(JSONObject newData){
        if(newData.has("endpoint"))
            this.endpoint = newData.getString("endpoint");
        if(newData.has("query"))
            this.query = newData.getString("query");
        if(newData.has("label"))
            this.label = newData.getString("label");
        if(newData.has("description"))
            this.description = newData.getString("description");
        if(newData.has("type"))
            this.type = Type.getByString(newData.getString("type"));
        if(newData.has("visibility"))
            this.setVisibility(Visibility.getByString(newData.getString("visibility")));
    }

    @JsonIgnore
    public Model getRDF(){
        Model result = ModelFactory.createDefaultModel();
        result.enterCriticalSection(false);

        try {
            Resource resource = result.createResource("http://www.freme-project.eu/data/templates/" + this.getId());
            result.add(resource, RDF.type, result.getResource("http://www.freme-project.eu/ns#Template"));
            result.add(resource, result.getProperty("http://www.freme-project.eu/ns#templateId"), this.getId()+"");
            result.add(resource, result.getProperty("http://www.freme-project.eu/ns#query"), this.getQuery());
            result.add(resource, result.getProperty("http://www.freme-project.eu/ns#endpoint"), this.getEndpoint());
            result.add(resource, RDFS.label, this.getLabel());
            result.add(resource, DCTerms.description, this.getDescription());
        } finally {
            result.leaveCriticalSection();
        }

        return result;
    }


    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

}
