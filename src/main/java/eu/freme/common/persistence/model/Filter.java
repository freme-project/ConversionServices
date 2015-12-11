package eu.freme.common.persistence.model;

import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.Model;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Arne on 11.12.2015.
 */
@Component
@Entity
@Table(name = "filter")
public class Filter extends OwnedResource {

    Query query;
    String name;

    Filter(){super();}

    Filter(Visibility visibility, String name, String queryString){
        super(visibility);
        this.name = name;
        this.query = QueryFactory.create(queryString);
    }

    Filter(String name, String queryString){
        super(Visibility.PUBLIC);
        this.name = name;
        this.query = QueryFactory.create(queryString);
    }

    Model getFilteredModel(Model model){
        QueryExecution qe = QueryExecutionFactory.create(query, model);
        return qe.execConstruct();
    }

    ResultSet getFilteredResultSet(Model model){
        QueryExecution qe = QueryExecutionFactory.create(query, model);
        return qe.execSelect();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }
}
