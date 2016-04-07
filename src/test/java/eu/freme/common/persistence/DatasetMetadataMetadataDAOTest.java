package eu.freme.common.persistence;

import eu.freme.common.FREMECommonConfig;
import eu.freme.common.persistence.dao.DatasetMetadataDAO;
import eu.freme.common.persistence.dao.UserDAO;
import eu.freme.common.persistence.model.DatasetMetadata;
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
public class DatasetMetadataMetadataDAOTest {

    @Autowired
    UserDAO userDAO;

    @Autowired
    DatasetMetadataDAO datasetMetadataDAO;

    private Logger logger = Logger.getLogger(DatasetMetadataMetadataDAOTest.class);

    @Test
    public void test() throws Exception {

        long countBefore = datasetMetadataDAO.count();

        logger.info("create user and save it");
        User user = new User("hallo", "welt", User.roleUser);
        user = userDAO.save(user);
        logger.info("create datasetMetadata");
        DatasetMetadata datasetMetadata = new DatasetMetadata(user, OwnedResource.Visibility.PUBLIC, "name", "description");

        //// does not work: the current session has to be authenticated for accessibility checks!
        // datasetDAO.save(datasetMetadata);
        //// use this instead (but the id will not be set correctly, so we have to do it manually):
        datasetMetadata.setId(1);
        datasetMetadataDAO.getRepository().save(datasetMetadata);
        ////

        long id = datasetMetadata.getId();
        logger.info("id of saved datasetMetadata: "+id);

        assertEquals(countBefore + 1, datasetMetadataDAO.count());

        logger.info("fetch and check saved datasetMetadata");
        DatasetMetadata fetchedDatasetMetadata = datasetMetadataDAO.getRepository().findOneById(id);
        assertEquals("name", fetchedDatasetMetadata.getName());
        assertEquals("description", fetchedDatasetMetadata.getDescription());
        assertEquals(0, fetchedDatasetMetadata.getTotalEntities());
        datasetMetadataDAO.getRepository().delete(fetchedDatasetMetadata);

        logger.info("delete datasetMetadata");
        assertEquals(countBefore, datasetMetadataDAO.count());
        userDAO.delete(user);
    }
}
