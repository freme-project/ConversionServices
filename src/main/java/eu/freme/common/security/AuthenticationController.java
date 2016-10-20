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

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import eu.freme.common.exception.AuthenticationFailedException;
import eu.freme.common.persistence.model.Token;

@RestController
public class AuthenticationController {

	@Autowired
	AuthenticationManager authenticationManager;
	
	Logger logger = Logger.getLogger(AuthenticationController.class);
	
	@RequestMapping(value = "/authenticate", method = RequestMethod.POST, produces="application/json")
	public ResponseEntity<String> authenticate(
			@RequestHeader(value = "X-Auth-Username", required = true) String username,
			@RequestHeader(value = "X-Auth-Password", required = true) String password) {
		
        UsernamePasswordAuthenticationToken requestAuthentication = new UsernamePasswordAuthenticationToken(username, password);
        Authentication resultOfAuthentication = null;
    	try{
            Authentication responseAuthentication = authenticationManager.authenticate(requestAuthentication);
            if (responseAuthentication == null || !responseAuthentication.isAuthenticated()) {
            	throw new AuthenticationFailedException();
            }    		
            logger.debug("User successfully authenticated");
            resultOfAuthentication = responseAuthentication;
    	} catch( Exception e){
    		logger.error(e);
    		throw new AuthenticationFailedException();
    	}

        SecurityContextHolder.getContext().setAuthentication(resultOfAuthentication);

        Token token = (Token)resultOfAuthentication.getDetails();
        
        JSONObject json = new JSONObject();
        json.put("token", token.getToken());

        ResponseEntity<String> response = new ResponseEntity<String>(json.toString(), HttpStatus.OK);
        return response;

	}
}

