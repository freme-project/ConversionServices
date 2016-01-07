package eu.freme.common.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import eu.freme.common.FREMECommonConfig;
import eu.freme.common.persistence.model.OwnedResource;
import eu.freme.common.persistence.model.Template;
import eu.freme.common.persistence.model.User;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Arne Binder (arne.b.binder@gmail.com) on 13.10.2015.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = FREMECommonConfig.class)
public class TemplateSerializationTest {

    private Logger logger = Logger.getLogger(TemplateSerializationTest.class);

    @Test
    public void templateSerialization() throws Exception {
        User owner = new User("name", "password", User.roleUser);
        Template template = new Template(owner,OwnedResource.Visibility.PUBLIC, Template.Type.SPARQL,"endpoint","query","label","description");

        ObjectMapper om = new ObjectMapper();
        //om.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);;
        ObjectWriter ow = om.writer().withDefaultPrettyPrinter();
        String serialization = ow.writeValueAsString(template);
        logger.info(serialization);


    }
}
