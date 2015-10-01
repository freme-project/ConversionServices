package eu.freme.common.persistence.model;


import com.fasterxml.jackson.databind.JsonSerializable;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.vocabulary.DCTerms;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.io.IOException;

/**
 * Created by Arne Binder (arne.b.binder@gmail.com) on 01.10.2015.
 */
@Entity
@Table(name = "template")
public class Template extends OwnedResource implements JsonSerializable {

    @Lob
    private String endpoint;
    @Lob
    private String query;
    @Lob
    private String label;
    @Lob
    private String description;

    public Template(User owner, Visibility visibility, String endpoint, String query, String label, String description) {
        super(null, owner, visibility);
        this.endpoint = endpoint;
        this.query = query;
        this.label = label;
        this.description = description;
    }
    public Template(Visibility visibility, String endpoint, String query, String label, String description) {
        super(null, visibility);
        this.endpoint = endpoint;
        this.query = query;
        this.label = label;
        this.description = description;
    }

    public Template(User owner, Visibility visibility, Model model){
        super(null, owner, visibility);
        setTemplateWithModel(model);
    }

    public Template(Visibility visibility, Model model){
        super(null, visibility);
        setTemplateWithModel(model);
    }

    public Template(){super();}


    public void setTemplateWithModel(Model model){
        model.enterCriticalSection(false);
        try {
            StmtIterator iter = model.listStatements((Resource) null, RDF.type, model.getResource("http://www.freme-project.eu/ns#Template"));

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

    public Model getRDF(){
        Model result = ModelFactory.createDefaultModel();
        result.enterCriticalSection(false);

        try {
            Resource resource = result.createResource("http://www.freme-project.eu/data/templates/" + this.getId());
            result.add(resource, RDF.type, result.getResource("http://www.freme-project.eu/ns#Template"));
            result.add(resource, result.getProperty("http://www.freme-project.eu/ns#templateId"), this.getId());
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

    @Override
    public void serialize(com.fasterxml.jackson.core.JsonGenerator jsonGenerator, com.fasterxml.jackson.databind.SerializerProvider serializerProvider) throws IOException, com.fasterxml.jackson.core.JsonProcessingException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("id", this.getId());
        jsonGenerator.writeStringField("visibility", this.getVisibility().name());
        jsonGenerator.writeStringField("endpoint", this.getEndpoint());
        jsonGenerator.writeStringField("query", this.getQuery());
        jsonGenerator.writeStringField("label", this.getLabel());
        jsonGenerator.writeStringField("description", this.getDescription());
        jsonGenerator.writeEndObject();
    }

    @Override
    public void serializeWithType(com.fasterxml.jackson.core.JsonGenerator jsonGenerator, com.fasterxml.jackson.databind.SerializerProvider serializerProvider, com.fasterxml.jackson.databind.jsontype.TypeSerializer typeSerializer) throws IOException, com.fasterxml.jackson.core.JsonProcessingException {
        /*typeSerializer.writeTypePrefixForScalar(this, jsonGenerator, Template.class);
        serialize(value, jsonGenerator, serializerProvider);
        typeSerializer.writeTypeSuffixForScalar(this, jsonGenerator);
        */
    }
}
