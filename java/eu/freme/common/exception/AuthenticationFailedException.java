package eu.freme.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.UNAUTHORIZED, reason="Authorization failed")
public class AuthenticationFailedException extends FREMEHttpException{

}
