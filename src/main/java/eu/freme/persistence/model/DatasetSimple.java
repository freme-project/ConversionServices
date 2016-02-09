package eu.freme.persistence.model;

import javax.persistence.*;

/**
 * Created by Arne Binder (arne.b.binder@gmail.com) on 15.10.2015.
 */
@Entity
@Table(name = "datasetSimple")
public class DatasetSimple {

    public DatasetSimple(){
        creationTime = System.currentTimeMillis();
    }

    public DatasetSimple(String name, String description, int totalEntities){
        creationTime = System.currentTimeMillis();
    }

    @Id @GeneratedValue
    private long id;

    private String name;

    @Lob
    private String description;

    private int totalEntities;

    private long creationTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTotalEntities() {
        return totalEntities;
    }

    public void setTotalEntities(int totalEntities) {
        this.totalEntities = totalEntities;
    }

    public long getCreationTime() {
        return creationTime;
    }
}
