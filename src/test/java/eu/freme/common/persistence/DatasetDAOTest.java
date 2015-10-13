package eu.freme.common.persistence;

import eu.freme.common.FREMECommonConfig;
import eu.freme.common.persistence.dao.DatasetDAO;
import eu.freme.common.persistence.dao.UserDAO;
import eu.freme.common.persistence.model.Dataset;
import eu.freme.common.persistence.model.OwnedResource;
import eu.freme.common.persistence.model.User;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

/**
 * Created by Arne Binder (arne.b.binder@gmail.com) on 13.10.2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = FREMECommonConfig.class)
public class DatasetDAOTest {

    @Autowired
    UserDAO userDAO;

    @Autowired
    DatasetDAO datasetDAO;

    private Logger logger = Logger.getLogger(DatasetDAOTest.class);

    @Test
    public void test() throws Exception {

        long countBefore = datasetDAO.count();

        logger.info("create user and save it");
        User user = new User("hallo", "welt", User.roleUser);
        userDAO.save(user);
        logger.info("create dataset");
        Dataset dataset = new Dataset(user, OwnedResource.Visibility.PUBLIC, "name", "description");

        // does not work: the current session have to be authenticated for accessibility checks!
        // datasetDAO.save(dataset);
        // use this instead:
        datasetDAO.getRepository().save(dataset);
        long id = dataset.getId();
        logger.info("id of saved dataset: "+id);

        assertEquals(countBefore+1,datasetDAO.count());

        logger.info("fetch and check saved dataset");
        Dataset fetchedDataset = datasetDAO.getRepository().findOneById(id);
        assertEquals("name", fetchedDataset.getName());
        assertEquals("description", fetchedDataset.getDescription());
        assertEquals(0, fetchedDataset.getTotalEntities());
        datasetDAO.getRepository().delete(fetchedDataset);

        assertEquals(countBefore,datasetDAO.count());
    }
}
