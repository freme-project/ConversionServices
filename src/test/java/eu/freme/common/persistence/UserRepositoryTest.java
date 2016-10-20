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
package eu.freme.common.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import eu.freme.common.FREMECommonConfig;
import eu.freme.common.persistence.dao.UserDAO;
import eu.freme.common.persistence.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = FREMECommonConfig.class)
public class UserRepositoryTest {

	@Autowired
	UserDAO userDAO;

	@Test
	public void testUserRepository(){


		long preexisting = userDAO.count();
		userDAO.save(new User("Juergen", "bla", User.roleUser));
		userDAO.save(new User("Peter", "bla", User.roleUser));
		userDAO.save(new User("Madeleine", "bla", User.roleAdmin));
		
		User juergen = userDAO.getRepository().findOneByName("Juergen");
		assertTrue(juergen!=null);

		// admin user is one more
		assertEquals(preexisting + 3, userDAO.count());

		userDAO.delete(juergen);
		assertEquals(preexisting + 2, userDAO.count());
		User peter = userDAO.getRepository().findOneByName("Peter");
		assertNotNull(peter);
		User madeleine = userDAO.getRepository().findOneByName("Madeleine");
		assertNotNull(madeleine);

		userDAO.delete(peter);
		userDAO.delete(madeleine);

		assertEquals(preexisting,userDAO.count());
	}
}
