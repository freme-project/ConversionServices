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
package eu.freme.common.security;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;

import eu.freme.common.persistence.model.Token;
import eu.freme.common.persistence.model.User;
import eu.freme.common.persistence.repository.TokenRepository;
/**
 * @author Jan Nehring - jan.nehring@dfki.de
 */
public class TokenService {

	@Autowired
	TokenRepository tokenRepository;

	public Token generateNewToken(User user) {
		Token token = new Token(UUID.randomUUID().toString(), user);
		token = tokenRepository.save(token);
		return token;
	}

	public Token retrieve(String tokenStr) {
		Token token = tokenRepository.findOneByToken(tokenStr);
		if (token == null) {
			throw new BadCredentialsException("invalid token");
		}
		return token;
	}
	
	public Token updateLastUsed(Token token){
		token.setLastUsedDate(new Date());
		token = tokenRepository.save(token);
		return token;
	}
}
