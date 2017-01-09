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
package eu.freme.common.exception;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Jan Nehring - jan.nehring@dfki.de
 */
@Service
@ControllerAdvice
public class ExceptionHandlerService {

	Logger logger = Logger.getLogger(ExceptionHandlerService.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleConflict(HttpServletRequest req, Exception e) {
    	return this.handleError(req, e);
    }

	/*
	 * Creates a nicely formated error message out of an exception and a
	 * HttpServletRequest.
	 * 
	 * @param req
	 * @param exception
	 * @return
	 */
	public ResponseEntity<String> handleError(HttpServletRequest req,
			Throwable exception) {
		logger.error("Request: " + req.getRequestURL() + " raised ", exception);

		HttpStatus statusCode = null;
		if (exception instanceof MissingServletRequestParameterException) {
			// create response for spring exceptions
			statusCode = HttpStatus.BAD_REQUEST;
		} else if (exception instanceof FREMEHttpException
				&& ((FREMEHttpException) exception).getHttpStatusCode() != null) {
			// get response code from FREMEHttpException
			statusCode = ((FREMEHttpException) exception).getHttpStatusCode();
		} else if (exception instanceof AccessDeniedException) {
			statusCode = HttpStatus.UNAUTHORIZED;
		} else if (exception instanceof HttpMessageNotReadableException) {
			statusCode = HttpStatus.BAD_REQUEST;
		} else {
			// get status code from exception class annotation
			Annotation responseStatusAnnotation = exception.getClass()
					.getAnnotation(ResponseStatus.class);
			if (responseStatusAnnotation instanceof ResponseStatus) {
				statusCode = ((ResponseStatus) responseStatusAnnotation)
						.value();
			} else {
				// set default status code 500
				statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
			}
		}
		JSONObject json = new JSONObject();
		json.put("status", statusCode.value());
		json.put("message", exception.getMessage());
		json.put("error", statusCode.getReasonPhrase());
		json.put("timestamp", new Date().getTime());
		json.put("exception", exception.getClass().getName());
		json.put("path", req.getRequestURI());

		if(exception instanceof AdditionalFieldsException){
			Map<String, JSONObject> additionalFields = ((AdditionalFieldsException) exception).getAdditionalFields();
			for(String key: additionalFields.keySet()){
				json.put(key, additionalFields.get(key));
			}
		}

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json");

		return new ResponseEntity<String>(json.toString(2), responseHeaders,
				statusCode);
	}

	/*
	 * This method can be used to send an exception to the user that was not
	 * thrown in the rest controller and is therefore not caught by the normal
	 * exception handler. It is useful to throw exceptions from a filter.
	 * 
	 * @throws IOException
	 */
	public void writeExceptionToResponse(HttpServletRequest request,
			HttpServletResponse response, Throwable exception)
			throws IOException {
		ResponseEntity<String> responseEntity = handleError(request, exception);
		response.setStatus(responseEntity.getStatusCode().value());
		response.getWriter().write(responseEntity.getBody());
		response.flushBuffer();
	}
}
