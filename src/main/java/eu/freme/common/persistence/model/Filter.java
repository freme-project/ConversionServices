package eu.freme.common.persistence.model;

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

    @Transient
    Query query;

    @Lob
    String queryString;
    String name;

    Filter(){super();}

    Filter(Visibility visibility, String name, String queryString){
        super(visibility);
        this.name = name;
        this.queryString = queryString;
    }

    Filter(String name, String queryString){
        super(Visibility.PUBLIC);
        this.name = name;
        this.queryString = queryString;
    }

    Model getFilteredModel(Model model){
        if(query==null)
            setQuery();
        QueryExecution qe = QueryExecutionFactory.create(query, model);
        return qe.execConstruct();
    }

    ResultSet getFilteredResultSet(Model model){
        if(query==null)
            setQuery();
        QueryExecution qe = QueryExecutionFactory.create(query, model);
        return qe.execSelect();
    }

    private void setQuery(){
        this.query = QueryFactory.create(queryString);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
