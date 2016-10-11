/**
 * Copyright (C) 2015 Agro-Know, Deutsches Forschungszentrum für Künstliche Intelligenz, iMinds,
 * Institut für Angewandte Informatik e. V. an der Universität Leipzig,
 * Istituto Superiore Mario Boella, Tilde, Vistatec, WRIPL (http://freme-project.eu)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.freme.common.rest;

import com.hp.hpl.jena.rdf.model.Model;
import eu.freme.common.conversion.SerializationFormatMapper;
import eu.freme.common.conversion.rdf.RDFConstants;
import eu.freme.common.conversion.rdf.RDFConversionService;
import eu.freme.common.exception.BadRequestException;
import eu.freme.common.exception.ExceptionHandlerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * Common codes for all rest controllers.
 * 
 * @author Jan Nehring - jan.nehring@dfki.de
 */
public abstract class BaseRestController {

	Logger logger = Logger.getLogger(BaseRestController.class);

	@Autowired
	RDFConversionService rdfConversionService;

	@Autowired
	NIFParameterFactory nifParameterFactory;

	@Autowired
	ExceptionHandlerService exceptionHandlerService;

	@Autowired
	SerializationFormatMapper serializationFormatMapper;

	@Autowired
	RestHelper restHelper;
	
	public static final String authenticationEndpoint = "/authenticate";

	public static final String inputDummy = "inputDummy";

	/**
	 * Create a NIFParameterSet to make dealing with NIF API specifications
	 * easier. It handles informat overwrites Content-Type header, input
	 * overwrites post body, and more.
	 * 
	 * @param input
	 * @param informat
	 * @param outformat
	 * @param postBody
	 * @param acceptHeader
	 * @param contentTypeHeader
	 * @param prefix
	 * @return
	 */
	protected NIFParameterSet normalizeNif(String input, String informat,
			String outformat, String postBody, String acceptHeader,
			String contentTypeHeader, String prefix) {
		return nifParameterFactory.constructFromHttp(input, informat,
				outformat, postBody, acceptHeader, contentTypeHeader, prefix);
	}

	/**
	 * Create a NIFParameterSet to make dealing with NIF API specifications
	 * easier. It handles informat overwrites Content-Type header, input
	 * overwrites post body, and more.
	 *
	 * @param postBody
	 * @param acceptHeader
	 * @param contentTypeHeader,
	 * @param parameters
	 * @param allowEmptyInput
	 */
	protected NIFParameterSet normalizeNif(String postBody, String acceptHeader, String contentTypeHeader, Map<String,String> parameters, boolean allowEmptyInput)
			throws BadRequestException {
		return restHelper.normalizeNif(postBody, acceptHeader, contentTypeHeader, parameters, allowEmptyInput);
	}
	
	/**
	 * Get NIFParameterSet from a HttpServletRequest. Throws an BadRequestException in case the input is not valid NIF.
	 * 
	 * @param request the request
	 * @param allowEmptyInput Specifies if it is allowed to send empty input text.
	 * @return
	 * @throws IOException
	 */
	protected NIFParameterSet normalizeNif(HttpServletRequest request, boolean allowEmptyInput) throws IOException{
		return restHelper.normalizeNif(request, allowEmptyInput);
	}

	/**
	 *
	 * @deprecated use {@link #serializeRDF(Model, String)} instead
     */
	@Deprecated
	protected String serializeRDF(Model model,
			RDFConstants.RDFSerialization format) throws Exception {
		return rdfConversionService.serializeRDF(model, format.contentType());
	}

	/**
	 * Convert Jena model to string.
	 *
	 * @param model
	 * @param format
	 * @return
	 * @throws Exception
	 */
	protected String serializeRDF(Model model,
								  String format) throws Exception {
		return rdfConversionService.serializeRDF(model, format);
	}

	/**
	 * @deprecated use {@link #unserializeRDF(String, String)} instead
     */
	@Deprecated
	protected Model unserializeRDF(String nif,
			RDFConstants.RDFSerialization format) throws Exception {
		return rdfConversionService.unserializeRDF(nif, format.contentType());
	}
	/**
	 * Convert string to Jena model.
	 *
	 * @param nif
	 * @param format
	 * @return
	 * @throws Exception
	 */
	protected Model unserializeRDF(String nif, String format) throws Exception {
		return rdfConversionService.unserializeRDF(nif, format);
	}

	/**
	 * Custom exception handler for all FREME endpoints. All exceptions (except
	 * security exceptions) get here.
	 * 
	 * @param req
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleError(HttpServletRequest req,
			Exception exception) {
		return exceptionHandlerService.handleError(req, exception);
	}

	/**
	 * @deprecated use {@link #createSuccessResponse(Model, String)} instead
	 */
	@Deprecated
	public ResponseEntity<String> createSuccessResponse(Model rdf,
			RDFConstants.RDFSerialization rdfFormat) {
		return restHelper.createSuccessResponse(rdf, rdfFormat.contentType());
	}

	/**
	 * Create a ResponseEntity for a REST API method. It accepts a Jena Model
	 * and an RDFSerialization format. It converts the model to a string in the
	 * desired serialization format and sets the right Content-Type header.
	 *
	 * @param rdf
	 * @param rdfFormat
	 * @return
	 */
	protected ResponseEntity<String> createSuccessResponse(Model rdf,
														String rdfFormat) {
		return restHelper.createSuccessResponse(rdf, rdfFormat);
	}

	protected RDFConversionService getRdfConversionService() {
		return rdfConversionService;
	}

	protected NIFParameterFactory getNifParameterFactory() {
		return nifParameterFactory;
	}

	protected RestHelper getRestHelper() {
		return restHelper;
	}

	protected SerializationFormatMapper getSerializationFormatMapper() {
		return serializationFormatMapper;
	}
}
