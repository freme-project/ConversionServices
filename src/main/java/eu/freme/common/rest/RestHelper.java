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
package eu.freme.common.rest;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import eu.freme.common.conversion.SerializationFormatMapper;
import eu.freme.common.conversion.rdf.RDFConstants;
import eu.freme.common.conversion.rdf.RDFConversionService;
import eu.freme.common.exception.BadRequestException;
import eu.freme.common.exception.InternalServerErrorException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class RestHelper {

	Logger logger = Logger.getLogger(RestHelper.class);

	@Autowired
	RDFConversionService rdfConversionService;

	@Autowired
	NIFParameterFactory nifParameterFactory;

	public String getDefaultPrefix() {
		return nifParameterFactory.getDefaultPrefix();
	}

	/**
	 * Create a NIFParameterSet to make dealing with NIF API specifications
	 * easier. It handles informat overwrites Content-Type header, input
	 * overwrites post body, and more.
	 * 
	 * @param input the nif input parameter
	 * @param informat the nif informat parameter
	 * @param outformat the nif outformat parameter
	 * @param postBody the http post body content
	 * @param acceptHeader the http accept header
	 * @param contentTypeHeader the http content-type header
	 * @param prefix the nif prefix parameter
	 * @return the created NifParameterSet
	 */
	public NIFParameterSet normalizeNif(String input, String informat,
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
	 * @param postBody the http post body content
	 * @param acceptHeader the http accept header
	 * @param contentTypeHeader the http content-type header
	 * @param parameters the http parameters containing the nif parameters
	 * @param allowEmptyInput if false, throw a BadRequestException if post http body and nif input is empty
	 * @return the created NIFParameterSet
     * @throws BadRequestException
     */
	public NIFParameterSet normalizeNif(String postBody, String acceptHeader,
			String contentTypeHeader, Map<String, String> parameters,
			boolean allowEmptyInput) throws BadRequestException {
		// merge long and short parameters - long parameters override short
		// parameters
		String input = parameters.get("input");
		if (input == null) {
			input = parameters.get("i");
		}
		// trim input and set it to null, if empty
		if (input != null) {
			input = input.trim();
			if (input.length() == 0)
				input = null;
		}

		String informat = parameters.get("informat");
		if (informat == null) {
			informat = parameters.get("f");
		}
		String outformat = parameters.get("outformat");
		if (outformat == null) {
			outformat = parameters.get("o");
		}
		String prefix = parameters.get("prefix");
		if (prefix == null) {
			prefix = parameters.get("p");
		}
		String nifVersion = parameters.get(NIFParameterFactory.versionIdentifier);

		return nifParameterFactory.constructFromHttp(input, informat,
				outformat, postBody, acceptHeader, contentTypeHeader, prefix, nifVersion,
				allowEmptyInput);
	}

	/**
	 * Get NIFParameterSet from a HttpServletRequest. Throws an
	 * BadRequestException in case the input is not valid NIF.
	 * 
	 * @param request
	 *            the request
	 * @param allowEmptyInput
	 *            Specifies if it is allowed to send empty input text.
	 * @return the new NifParameterSet
	 */
	public NIFParameterSet normalizeNif(HttpServletRequest request,
			boolean allowEmptyInput) {

		try {
			BufferedReader reader = request.getReader();
			StringBuilder bldr = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				bldr.append(line);
				bldr.append("\n");
			}

			String postBody = bldr.toString();
			String acceptHeader = request.getHeader("accept");
			String contentTypeHeader = request.getHeader("content-type");

			Map<String, String> parameters = new HashMap<>();
			for (String key : request.getParameterMap().keySet()) {
				parameters.put(key, request.getParameter(key));
			}
			return normalizeNif(postBody, acceptHeader, contentTypeHeader,
					parameters, allowEmptyInput);
		} catch (IOException e) {
			logger.error(e);
			throw new InternalServerErrorException();
		}

	}

	/**
	 * @deprecated use {@link #createSuccessResponse(Model, String)} instead
     */
	@Deprecated
	public ResponseEntity<String> createSuccessResponse(Model rdf,
			RDFConstants.RDFSerialization rdfFormat) {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", rdfFormat.contentType());
		String rdfString;
		try {
			rdfString = rdfConversionService.serializeRDF(rdf, rdfFormat.contentType());
		} catch (Exception e) {
			throw new InternalServerErrorException();
		}
		return new ResponseEntity<>(rdfString, responseHeaders, HttpStatus.OK);
	}

	/**
	 * Create a ResponseEntity for a REST API method. It accepts a Jena Model
	 * and an RDFSerialization format. It converts the model to a string in the
	 * desired serialization format and sets the right Content-Type header.
	 *
	 * @param rdf the jena rdf model
	 * @param rdfFormat the serialization output format
	 * @return the created success response entity
	 */
	public ResponseEntity<String> createSuccessResponse(Model rdf, String rdfFormat) {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", rdfFormat);
		String rdfString;
		try {
			rdfString = rdfConversionService.serializeRDF(rdf, rdfFormat);
		} catch (Exception e) {
			throw new InternalServerErrorException();
		}
		return new ResponseEntity<>(rdfString, responseHeaders, HttpStatus.OK);
	}

	/**
	 * Convert a NIFParameterSet to RDF model, from both plaintext input as from
	 * RDF in various serialization formats.
	 * 
	 * @param parameters the NifParameterSet to create the model from
	 * @return the created jena rdf model
	 */
	public Model convertInputToRDFModel(NIFParameterSet parameters) {

		// create rdf model
		Model model = ModelFactory.createDefaultModel();

		if (!parameters.getInformatString().equals(
				SerializationFormatMapper.PLAINTEXT)) {
			// input is nif
			try {
				model = rdfConversionService.unserializeRDF(parameters.getInput(),
						parameters.getInformatString());
				return model;
			} catch (Exception e) {
				logger.error("failed", e);
				throw new BadRequestException("Error parsing NIF input");
			}
		} else {
			// input is plaintext
			rdfConversionService.plaintextToRDF(model, parameters.getInput(),
					null, parameters.getPrefix(), parameters.getNifVersion());
			return model;
		}

	}


	public HttpResponse<String> sendNifRequest(NIFParameterSet parameters, String url) throws UnirestException {
		String prefix = parameters.getPrefix();
		if(prefix == null)
			prefix = nifParameterFactory.getDefaultPrefix();
		return Unirest.post(url)
				.header("Content-Type", parameters.getInformatString())
				.header("Accept", parameters.getOutformatString())
				.queryString("prefix", prefix)
				.body(parameters.getInput())
				.asString();
	}
}
