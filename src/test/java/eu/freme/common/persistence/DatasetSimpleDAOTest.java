package eu.freme.common.persistence;

import eu.freme.common.FREMECommonConfig;
import eu.freme.persistence.dao.DatasetSimpleDAO;
import eu.freme.persistence.model.DatasetSimple;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

/**
 * Created by Arne Binder (arne.b.binder@gmail.com) on 15.10.2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = FREMECommonConfig.class)
public class DatasetSimpleDAOTest {

    @Autowired
    DatasetSimpleDAO datasetSimpleDAO;

    private Logger logger = Logger.getLogger(DatasetSimpleDAOTest.class);

    @Test
    public void test() throws Exception {
        DatasetSimple dataset = new DatasetSimple();
        long count = datasetSimpleDAO.count();
        logger.info("new Dataset id before save: "+dataset.getId());
        dataset.setName("TEST");
        dataset = datasetSimpleDAO.save(dataset);

        logger.info("new Dataset id after save: "+dataset.getId());
        assertEquals(count+1,datasetSimpleDAO.count());
        datasetSimpleDAO.delete(dataset);
        assertEquals(count,datasetSimpleDAO.count());

    }
}
