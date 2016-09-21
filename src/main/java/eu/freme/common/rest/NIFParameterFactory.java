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

import eu.freme.common.conversion.SerializationFormatMapper;
import eu.freme.common.conversion.rdf.RDFConstants;
import eu.freme.common.exception.BadRequestException;
import eu.freme.common.exception.UnsupportedRDFSerializationException;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static eu.freme.common.conversion.rdf.RDFConstants.TURTLE;

/**
 * Helper class to create a NIFParameterSet according to the specification of NIF.
 * 
 * @author Jan Nehring - jan.nehring@dfki.de
 */
public class NIFParameterFactory {

	@Autowired
	SerializationFormatMapper serializationFormatMapper;

	@Deprecated
	String defaultPrefix = "http://freme-project.eu/";

	public final Set<String> NIF_PARAMETERS = new HashSet<>(Arrays.asList(new String[]{
			"input", "i", "informat", "f", "outformat", "o", "prefix", "p"
	}));


	public NIFParameterSet constructFromHttp(String input, String informat,
											 String outformat, String postBody, String acceptHeader,
											 String contentTypeHeader, String prefix) throws BadRequestException {
		return constructFromHttp(input, informat, outformat, postBody, acceptHeader, contentTypeHeader, prefix, false);
	}


	public NIFParameterSet constructFromHttp(String input, String informat,
			String outformat, String postBody, String acceptHeader,
			String contentTypeHeader, String prefix, boolean allowEmptyInput) throws BadRequestException {

		String thisInput;
		if (!allowEmptyInput && input == null && postBody == null) {
			throw new BadRequestException("no input found in request");
		} else if (input != null) {
			thisInput = input;
		} else {
			thisInput = postBody;
		}

		String thisInformat;
		if (informat == null && contentTypeHeader == null) {
			thisInformat = TURTLE;
		} else if (informat != null) {
			thisInformat = serializationFormatMapper.get(informat);
			if (thisInformat==null) {
				throw new BadRequestException(
						"parameter informat has invalid value \"" + informat
								+ "\"");
			}
		} else {
			String[] contentTypeHeaderParts = contentTypeHeader.split(";");
			thisInformat = serializationFormatMapper.get(contentTypeHeaderParts[0]);
			if (thisInformat==null) {
				throw new BadRequestException(
						"Content-Type header has invalid value \""
								+ contentTypeHeader + "\"");
			}
		}
		if(!RDFConstants.SERIALIZATION_FORMATS.contains(thisInformat) && !thisInformat.equals(SerializationFormatMapper.PLAINTEXT)){
			throw new UnsupportedRDFSerializationException(
					"Parameter informat has invalid value \"" + thisInformat
							+ "\"");
		}

		String thisOutformat;
		if( acceptHeader != null && acceptHeader.equals("*/*")){
			acceptHeader = TURTLE;
		}
		if (outformat == null && acceptHeader == null) {
			thisOutformat = TURTLE;
		} else if (outformat != null) {
			thisOutformat = serializationFormatMapper.get(outformat);
			if (thisOutformat==null) {
				throw new BadRequestException(
						"Parameter outformat has invalid value \"" + outformat
								+ "\"");
			}
		} else {
			thisOutformat = serializationFormatMapper.get(acceptHeader.split(";")[0]);
			if (thisOutformat==null) {
				throw new BadRequestException(
						"Accept header has invalid value \""
								+ acceptHeader.split(";")[0] + "\"");
			}
		}
		if(!RDFConstants.SERIALIZATION_FORMATS.contains(thisOutformat)){
			throw new UnsupportedRDFSerializationException(
					"Parameter outformat has invalid value \"" + thisOutformat
							+ "\"");
		}

		String thisPrefix;
		if (prefix == null) {
			thisPrefix = RDFConstants.fremePrefix;
		} else{
			thisPrefix = prefix;
		}
		String[] schemes = {"http","https"};
		UrlValidator urlValidator = new UrlValidator(schemes);
		if (!urlValidator.isValid(thisPrefix)) {
			throw new BadRequestException("invalid prefix");
		}

		return new NIFParameterSet(thisInput, thisInformat, thisOutformat, thisPrefix);
	}

	public boolean isNIFParameter(String parameter){
		return NIF_PARAMETERS.contains(parameter);
	}

	/*
	 * @deprecated use RDFConstants.fremePrefix instead
	 */
	@Deprecated
	public String getDefaultPrefix() {
		return defaultPrefix;
	}
}
