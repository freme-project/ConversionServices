/**
 * Copyright © 2015 Agro-Know, Deutsches Forschungszentrum für Künstliche Intelligenz, iMinds,
 * Institut für Angewandte Informatik e. V. an der Universität Leipzig,
 * Istituto Superiore Mario Boella, Tilde, Vistatec, WRIPL (http://freme-project.eu)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.freme.common.persistence.ownedresource;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
		
		phoneService.createEntities();
		assertTrue(phoneService.countPhoneNumbers() == 1);
		
		phoneService.deletePhone();
		assertTrue(phoneService.countPhoneNumbers() == 0);
		assertTrue(phoneService.countUsers() > 0);
	}
}
