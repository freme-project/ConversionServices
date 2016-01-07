/**
 * Copyright (C) 2015 Agro-Know, Deutsches Forschungszentrum für Künstliche Intelligenz, iMinds,
 * Institut für Angewandte Informatik e. V. an der Universität Leipzig,
 * Istituto Superiore Mario Boella, Tilde, Vistatec, WRIPL (http://freme-project.eu)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.freme.common.persistence;

import eu.freme.common.FREMECommonConfig;
import eu.freme.common.persistence.dao.PipelineDAO;
import eu.freme.common.persistence.dao.UserDAO;
import eu.freme.common.persistence.model.OwnedResource;
import eu.freme.common.persistence.model.Pipeline;
import eu.freme.common.persistence.model.User;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * <p>Copyright 2015 MMLab, UGent</p>
 *
 * @author Gerald Haesendonck
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = FREMECommonConfig.class)
public class PipelineRepositoryTest {

	private Logger logger = Logger.getLogger(PipelineRepositoryTest.class);

	@Autowired
	private PipelineDAO pipelineDAO;

	@Autowired
	private UserDAO userDAO;

	@Test
	public void testPipelineRepository() {
		long pipelineCountBefore = pipelineDAO.count();
		long userCountBefore = userDAO.count();

		logger.info("Create user");
		User user = new User("hallo", "wereld", User.roleUser);
		user = userDAO.save(user);

		AuthenticationManager am = new SampleAuthenticationManager();
		Authentication request = new UsernamePasswordAuthenticationToken(user, user.getPassword());
		Authentication result = am.authenticate(request);
		SecurityContextHolder.getContext().setAuthentication(result);

		assertTrue(userDAO.findAll().iterator().hasNext());

		logger.info("Create pipeline");
		Pipeline pipeline = new Pipeline(user, OwnedResource.Visibility.PRIVATE, "label1", "description1", "first no real pipeline", false);
		pipeline = pipelineDAO.save(pipeline);
		assertTrue(pipelineDAO.findAll().iterator().hasNext());
		logger.info("Pipeline count: " + pipelineDAO.count());

		logger.info("create 2nd pipeline");
		Pipeline pipeline2 = new Pipeline(user, OwnedResource.Visibility.PUBLIC, "label2", "description2", "second no real pipeline", true);
		pipeline = pipelineDAO.save(pipeline2);
		assertEquals(pipelineCountBefore + 2, pipelineDAO.count());

		Pipeline pipeline1FromStore = pipelineDAO.findOneById(pipeline.getId());
		assertEquals(pipeline, pipeline1FromStore);

		Pipeline pipeline2FromStore = pipelineDAO.findOneById(pipeline2.getId());
		assertEquals(pipeline2, pipeline2FromStore);

		logger.info("Deleting first pipeline");
		pipelineDAO.delete(pipeline);
		assertEquals(pipelineCountBefore + 1, pipelineDAO.count());

		logger.info("Delining user, should also delete second pipeline.");
		userDAO.delete(user);
		assertEquals(userCountBefore, userDAO.count());
		assertEquals(pipelineCountBefore, pipelineDAO.count());
	}

	class SampleAuthenticationManager implements AuthenticationManager {

		@Override
		public Authentication authenticate(Authentication authentication) throws AuthenticationException {
			return authentication;
		}
	}

}
