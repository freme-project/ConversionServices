package eu.freme.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="")
public class NIFVersionNotSupportedException extends FREMEHttpException {

	public NIFVersionNotSupportedException(String msg){
		super(msg);
	}
}
