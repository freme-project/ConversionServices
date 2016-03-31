package eu.freme.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Arne Binder (arne.b.binder@gmail.com) on 01.10.2015.
 */
@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="")
public class FileNotFoundException extends FREMEHttpException {
    public FileNotFoundException(String msg){
            super(msg);
        }
}
