package eu.freme.common.persistence.ownedresource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertTrue;
import eu.freme.common.FREMECommonConfig;

/**
 * Test that if a phone number is attached to the user it is deleted when the
 * user is deleted, even when no reference to the phone number is stored in the
 * user entity.
 * 
 * @author jnehring
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = FREMECommonConfig.class)
public class OwnedResourceTest {

	@Autowired
	PhoneService phoneService;

	@Test
	public void testOwnedResource() {

		phoneService.createEntities();
		assertTrue(phoneService.countPhoneNumbers() == 1);

		phoneService.deleteUser();
		assertTrue(phoneService.countPhoneNumbers() == 0);
	}
}
