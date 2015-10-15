package eu.freme.common.persistence;

import eu.freme.common.persistence.dao.DatasetSimpleDAO;
import eu.freme.common.persistence.model.DatasetSimple;
import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

/**
 * Created by Arne Binder (arne.b.binder@gmail.com) on 15.10.2015.
 */
@Ignore
public class DatasetSimpleDAOTest {

    @Autowired
    DatasetSimpleDAO datasetSimpleDAO;

    private Logger logger = Logger.getLogger(DatasetSimpleDAOTest.class);

    @Test
    public void test() throws Exception {
        DatasetSimple dataset = new DatasetSimple();
        long count = datasetSimpleDAO.count();
        dataset.setName("TEST");
        datasetSimpleDAO.save(dataset);

        assertEquals(count+1,datasetSimpleDAO.count());
        datasetSimpleDAO.delete(dataset);
        assertEquals(count,datasetSimpleDAO.count());

    }
}
