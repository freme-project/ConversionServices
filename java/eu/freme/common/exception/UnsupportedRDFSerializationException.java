package eu.freme.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Arne on 21.09.2016.
 */
@SuppressWarnings("serial")
@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="")
public class UnsupportedRDFSerializationException extends RuntimeException{

    public UnsupportedRDFSerializationException(String msg){
        super(msg);
    }
}
