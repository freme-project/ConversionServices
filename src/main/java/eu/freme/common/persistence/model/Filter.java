package eu.freme.common.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.Model;
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
        QueryExecution qe = QueryExecutionFactory.create(query, model);
        return qe.execConstruct();
    }

    public ResultSet getFilteredResultSet(Model model){
        QueryExecution qe = QueryExecutionFactory.create(query, model);
        return qe.execSelect();
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
