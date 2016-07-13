package eu.freme.common.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import eu.freme.common.conversion.rdf.RDFConstants;
import eu.freme.common.conversion.rdf.RDFConversionService;
import eu.freme.common.conversion.rdf.RDFSerializationFormats;
import eu.freme.common.exception.BadRequestException;
import eu.freme.common.exception.InternalServerErrorException;

@Component
public class RestHelper {

	Logger logger = Logger.getLogger(RestHelper.class);

	@Autowired
	RDFConversionService rdfConversionService;

	@Autowired
	NIFParameterFactory nifParameterFactory;

	@Autowired
	RDFSerializationFormats rdfSerializationFormats;

	//String defaultPrefix = "http://freme-project.eu/";

	public String getDefaultPrefix() {
		return nifParameterFactory.getDefaultPrefix();
	}

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
	 * @param postBody
	 * @param acceptHeader
	 * @param contentTypeHeader
	 *            ,
	 * @param parameters
	 * @param allowEmptyInput
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
		return nifParameterFactory.constructFromHttp(input, informat,
				outformat, postBody, acceptHeader, contentTypeHeader, prefix,
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
	 * @return
	 * @throws IOException
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

			Map<String, String> parameters = new HashMap<String, String>();
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
	 * Convert Jena model to string.
	 * 
	 * @param model
	 * @param format
	 * @return
	 * @throws Exception
	 */
	public String serializeNif(Model model, RDFConstants.RDFSerialization format)
			throws Exception {
		return rdfConversionService.serializeRDF(model, format);
	}

	/**
	 * Convert string to Jena model.
	 * 
	 * @param nif
	 * @param format
	 * @return
	 * @throws Exception
	 */
	public Model unserializeNif(String nif, RDFConstants.RDFSerialization format)
			throws Exception {
		return rdfConversionService.unserializeRDF(nif, format);
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
	public ResponseEntity<String> createSuccessResponse(Model rdf,
			RDFConstants.RDFSerialization rdfFormat) {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", rdfFormat.contentType());
		String rdfString;
		try {
			rdfString = serializeNif(rdf, rdfFormat);
		} catch (Exception e) {
			throw new InternalServerErrorException();
		}
		return new ResponseEntity<>(rdfString, responseHeaders, HttpStatus.OK);
	}

	/**
	 * Convert a NIFParameterSet to RDF model, from both plaintext input as from
	 * RDF in various serialization formats.
	 * 
	 * @param parameters
	 * @return
	 */
	public Model convertInputToRDFModel(NIFParameterSet parameters) {

		// create rdf model
		Model model = ModelFactory.createDefaultModel();

		if (!parameters.getInformat().equals(
				RDFConstants.RDFSerialization.PLAINTEXT)) {
			// input is nif
			try {
				model = this.unserializeNif(parameters.getInput(),
						parameters.getInformat());
				return model;
			} catch (Exception e) {
				logger.error("failed", e);
				throw new BadRequestException("Error parsing NIF input");
			}
		} else {
			// input is plaintext
			rdfConversionService.plaintextToRDF(model, parameters.getInput(),
					null, parameters.getPrefix());
			return model;
		}

	}


	public HttpResponse<String> sendNifRequest(NIFParameterSet parameters, String url) throws UnirestException {
		String prefix = parameters.getPrefix();
		if(prefix == null)
			prefix = nifParameterFactory.getDefaultPrefix();
		return Unirest.post(url)
				.header("Content-Type", parameters.getInformat().contentType())
				.header("Accept", parameters.getOutformat().contentType())
				.queryString("prefix", prefix)
				.body(parameters.getInput())
				.asString();
	}
}
