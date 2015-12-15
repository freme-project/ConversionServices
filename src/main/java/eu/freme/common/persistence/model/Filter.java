package eu.freme.common.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.Model;
import eu.freme.common.exception.FREMEHttpException;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Created by Arne on 11.12.2015.
 */
@Component
@Entity
@Table(name = "filter")
public class Filter extends OwnedResource {

    @JsonIgnore
    @Transient
    Query jenaQuery;

    @Lob
    String query;
    String name;

    public Filter(){super();}

    public Filter(Visibility visibility, String name, String queryString){
        super(visibility);
        this.name = name;
        this.query = queryString;
        constructQuery();
    }

    public Filter(String name, String queryString){
        super(Visibility.PUBLIC);
        this.name = name;
        this.query = queryString;
        constructQuery();
    }

    public Model getFilteredModel(Model model){
        if(jenaQuery.isConstructType()) {
            QueryExecution qe = QueryExecutionFactory.create(jenaQuery, model);
            return qe.execConstruct();
        }else
            throw new FREMEHttpException("The executed query does not return a RDF graph. Current Jena query type: "+jenaQuery.getQueryType()+", see https://jena.apache.org/documentation/javadoc/arq/constant-values.html section org.apache.jena.query.Query");
    }

    public ResultSet getFilteredResultSet(Model model){
        if(jenaQuery.isSelectType()) {
            QueryExecution qe = QueryExecutionFactory.create(jenaQuery, model);
            return qe.execSelect();
        }else
            throw new FREMEHttpException("The executed query does not return a set of tuples. Current Jena query type: "+jenaQuery.getQueryType()+", see https://jena.apache.org/documentation/javadoc/arq/constant-values.html section org.apache.jena.query.Query");
    }

    public int getQueryType(){
        return jenaQuery.getQueryType();
    }

    public void constructQuery(){
        this.jenaQuery = QueryFactory.create(query);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String queryString) {
        this.query = queryString;
        constructQuery();
    }
}
