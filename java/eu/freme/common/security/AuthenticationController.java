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
