package eu.freme.common.persistence.model;

import javax.persistence.*;

/**
 * Created by Arne Binder (arne.b.binder@gmail.com) on 01.10.2015.
 */
@Entity
@Table(name = "dataset")
public class Dataset extends OwnedResource{
    public Dataset(String id, Visibility visibility) {
        super(id, visibility);
    }
    public Dataset(String id, User owner, Visibility visibility) {
        super(id, owner, visibility);
    }

    public Dataset(){super();}
}

